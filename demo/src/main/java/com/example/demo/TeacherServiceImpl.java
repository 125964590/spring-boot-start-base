package com.example.demo;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.huatu.tiku.schedule.base.exception.BadRequestException;
import com.huatu.tiku.schedule.base.service.impl.BaseServiceImpl;
import com.huatu.tiku.schedule.biz.bean.TeacherScoreBean;
import com.huatu.tiku.schedule.biz.domain.Teacher;
import com.huatu.tiku.schedule.biz.domain.TeacherSubject;
import com.huatu.tiku.schedule.biz.enums.ExamType;
import com.huatu.tiku.schedule.biz.enums.TeacherCourseLevel;
import com.huatu.tiku.schedule.biz.enums.TeacherStatus;
import com.huatu.tiku.schedule.biz.enums.TeacherType;
import com.huatu.tiku.schedule.biz.repository.InterviewTeacherRepository;
import com.huatu.tiku.schedule.biz.repository.TeacherRepository;
import com.huatu.tiku.schedule.biz.repository.TeacherSubjectRepository;
import com.huatu.tiku.schedule.biz.vo.RoleManageVo;
import com.huatu.tiku.schedule.biz.vo.TeacherVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class TeacherServiceImpl extends BaseServiceImpl<Teacher, Long> implements TeacherService {

	private final TeacherRepository teacherRepository;
	private final TeacherSubjectRepository teacherSubjectRepository;

	@Autowired
	public TeacherServiceImpl(TeacherRepository teacherRepository,TeacherSubjectRepository teacherSubjectRepository) {
		this.teacherRepository = teacherRepository;
		this.teacherSubjectRepository = teacherSubjectRepository;
	}

	@Override
	public List<Teacher> getAvailable(Integer date, Integer timeBegin, Integer timeEnd) {
		// 获取教师资源，带有课程明细

		// 遍历，过滤已经有安排的教师

		return null;
	}

	@Override
	public Teacher saveX(Teacher teacher,List<TeacherSubject> teacherSubjects){
        boolean exists = teacherRepository.existsByPhone(teacher.getPhone());
        if(exists){
            throw new BadRequestException("该手机号码已注册");
        }
        teacher = teacherRepository.save(teacher);
		Long teacherId = teacher.getId();//主键id
		if(teacherSubjects!=null&&!teacherSubjects.isEmpty()){
			Teacher newTeacher = new Teacher();
			newTeacher.setId(teacherId);
			Iterator<TeacherSubject> iterator = teacherSubjects.iterator();
			while(iterator.hasNext()){//添加主键
				TeacherSubject subject=iterator.next();
				if(subject.getExamType()==null||subject.getSubjectId()==null||subject.getTeacherCourseLevel()==null){//有一个为空删除
					iterator.remove();
				}else{//都不为空添加主键
					subject.setTeacher(newTeacher);
				}
			}
			teacherSubjectRepository.save(teacherSubjects);
		}
		teacher.setPassword("");
		return  teacher;
	}

	@Override
	public Page getTeacherList(ExamType examType, String name,Long id, Long subjectId,
										Boolean leaderFlag, TeacherStatus status, TeacherType teacherType,Pageable page) {

		Specification<Teacher> querySpecific = new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery,
										 CriteriaBuilder criteriaBuilder) {

				List<Predicate> predicates = new ArrayList<>();
				if (examType != null) {
					predicates.add(criteriaBuilder.equal(root.get("examType"), examType));
				}if(!Strings.isNullOrEmpty(name)){
					predicates.add(criteriaBuilder.like(root.get("name"),"%"+name+"%"));
				}if (id != null) {
					predicates.add(criteriaBuilder.equal(root.get("id"), id));
				}
				if (subjectId != null) {
					predicates.add(criteriaBuilder.equal(root.get("subjectId"), subjectId));
				}
				if (leaderFlag != null) {
					predicates.add(criteriaBuilder.equal(root.get("leaderFlag"), leaderFlag ));
				}
				if (status != null) {
					predicates.add(criteriaBuilder.equal(root.get("status"), status));
				}
				if (teacherType != null) {
					if(teacherType.equals(TeacherType.JS)){//讲师类型
						predicates.add(criteriaBuilder.equal(root.get("teacherType"), TeacherType.JS));
					}else{
						predicates.add(criteriaBuilder.notEqual(root.get("teacherType"), TeacherType.JS));
					}
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		Page<Teacher> teachers = teacherRepository.findAll(querySpecific, page);
		List<Teacher> content = teachers.getContent();
		List teacherVos=new ArrayList();
		if(content!=null&&!content.isEmpty()){
            for(Teacher teacher:content) {
                TeacherVo teacherVo = new TeacherVo(teacher);
                teacherVos.add(teacherVo);
            }
        }
		PageImpl<TeacherVo> pageTeacheres=new PageImpl(teacherVos,page,teachers == null ? 0 : teachers.getTotalElements());
		return pageTeacheres;
	}

	@Override
	public int updateTeacherStatus(List<Long> ids, TeacherStatus status) {
		return teacherRepository.updateTeacherStatus(ids,status);
	}

	@Override
	public List<Teacher> getAvailableTeacher(Date date, ExamType examType, Integer timeBegin, Integer timeEnd, Long subjectId, TeacherCourseLevel teacherCourseLevel) {
		List<Long> leverTeachers=null;
		if(subjectId!=null){
			leverTeachers = teacherRepository.getlevelTeacher(subjectId,teacherCourseLevel.ordinal());//取出符合级别要求ids
		}else {
			leverTeachers = teacherRepository.getlevelTeacherByExamType(examType.ordinal(),teacherCourseLevel.ordinal());//取出符合级别要求ids
		}
        if(leverTeachers==null||leverTeachers.isEmpty()){
            return null;//没有符合级别的直接返回
        }
        List<Long> UnavailableTeacherIds = teacherRepository.getUnavailableTeacherIds(date, timeBegin, timeEnd);//取出已经被占用ids
        UnavailableTeacherIds.remove(null);//移除not in集合中null元素
		List<Long> finalLeverTeachers = leverTeachers;
		Specification<Teacher> querySpecific = new Specification<Teacher>() {
            @Override
            public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (null!=UnavailableTeacherIds&&!UnavailableTeacherIds.isEmpty()) {//ids有不为null元素添加判断条件
                    predicates.add(root.get("id").in(UnavailableTeacherIds).not());
                }
                if(finalLeverTeachers!=null){
					predicates.add(root.get("id").in(finalLeverTeachers));
				}
                predicates.add(criteriaBuilder.equal(root.get("teacherType"), TeacherType.JS));//讲师类型
                predicates.add(criteriaBuilder.equal(root.get("status"), TeacherStatus.ZC));//审核状态
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<Teacher> teachers = teacherRepository.findAll(querySpecific);
        teachers.forEach(teacher -> teacher.setPassword(""));//密码清空
        return teachers;
	}

	@Override
	public List<TeacherScoreBean> getAvailableTeachers(Date date, Integer timeBegin, Integer timeEnd, ExamType examType,
			Long subjectId, TeacherCourseLevel teacherCourseLevel) {
		// 获取所有符合条件的教师
		List<Teacher> teachers = teacherRepository.findAll(new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery,
					CriteriaBuilder criteriaBuilder) {
				criteriaQuery.distinct(true);

				List<Predicate> predicates = new ArrayList<>();

				Join<Teacher, TeacherSubject> teacherSubjects = root.join("teacherSubjects");

				if (examType != null && subjectId == null) {
					predicates.add(criteriaBuilder.equal(teacherSubjects.get("examType"), examType));
				}

				if (subjectId != null) {
					predicates.add(criteriaBuilder.equal(teacherSubjects.get("subjectId"), subjectId));
				}

				if (teacherCourseLevel != null) {
					predicates
							.add(criteriaBuilder.greaterThanOrEqualTo(teacherSubjects.get("teacherCourseLevel"), teacherCourseLevel));
				}

				// 审核状态
				predicates.add(criteriaBuilder.equal(root.get("status"), TeacherStatus.ZC));
				// 教师类型
				predicates.add(criteriaBuilder.equal(root.get("teacherType"), TeacherType.JS));

				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});

		List<TeacherScoreBean> teacherScoreBeans = Lists.newArrayList();
		teachers.forEach(teacher -> {
			TeacherScoreBean teacherScoreBean = new TeacherScoreBean();
			teacherScoreBean.setId(teacher.getId());
			teacherScoreBean.setName(teacher.getName());

			teacherScoreBeans.add(teacherScoreBean);
		});

		Iterator<TeacherScoreBean> teacherScoreBeanIterator = teacherScoreBeans.iterator();
		while (teacherScoreBeanIterator.hasNext()) {
			TeacherScoreBean teacherScoreBean = teacherScoreBeanIterator.next();

			// 获取上课时间和请假时间
			List<Object[]> times = teacherRepository.getUnavailableTime(teacherScoreBean.getId(), date);
			for (Object[] time : times) {
				Integer begin = (Integer) time[1];
				Integer end = (Integer) time[2];

				if ((timeEnd > begin && timeEnd < end) || (timeBegin > begin && timeBegin < end)
						|| (timeBegin < begin && timeEnd > end)) {
					// 时间有冲突
					teacherScoreBeanIterator.remove();
					break;
				}
			}
		}

		return teacherScoreBeans;
	}

	@Override
	public List<Teacher> findByTeacherTypeAndStatus(TeacherType teacherType, TeacherStatus teacherStatus) {
		return teacherRepository.findByTeacherTypeAndStatus(teacherType, teacherStatus);
	}

    @Override
    public List<Teacher> getAvailableAssistant(Date date, Integer timeBegin, Integer timeEnd, TeacherType teacherType) {
		List<Long> ids=null;
		switch (teacherType){//不同集合查找不同ids
			case ZJ:
				ids= teacherRepository.getUnavailableAssistantIds(date, timeBegin, timeEnd);
				break;
			case ZCR:
				ids= teacherRepository.getUnavailableCompereIds(date, timeBegin, timeEnd);
				break;
			case CK:
				ids= teacherRepository.getUnavailableControllerIds(date, timeBegin, timeEnd);
				break;
			case XXS:
				ids= teacherRepository.getUnavailableLearningTeacherIds(date, timeBegin, timeEnd);
				break;
			default://TODO查询空闲状态讲师
		}
		List<Long> idList=ids;
		idList.remove(null);//移除not in集合中null元素
		Specification<Teacher> querySpecific = new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery,
										 CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (null!=idList&&!idList.isEmpty()) {//ids有不为null元素添加判断条件
					predicates.add(root.get("id").in(idList).not());
				}
				if (teacherType != null) {//查找指定类型
					predicates.add(criteriaBuilder.equal(root.get("teacherType"), teacherType));
				}
				predicates.add(criteriaBuilder.equal(root.get("status"), 1));//审核状态
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		List<Teacher> teachers = teacherRepository.findAll(querySpecific);
		teachers.forEach(teacher -> teacher.setPassword(""));//密码清空
		return teachers;
    }

	@Override
	public Teacher findByPhone(String phone) {
		return teacherRepository.findByPhone(phone);
	}

	@Override
	public Set<String> getAuthorities(Long id) {
		return teacherRepository.getAuthorities(id);
	}

	@Override
	public List<Teacher> findByExamTypeAndSubjectId(ExamType examType, Long subjectId) {
		return teacherRepository.findByExamTypeAndSubjectId(examType, subjectId);
	}

	@Override
	public PageImpl<TeacherVo> findInterviewTeacher(Pageable page) {
		List leverTeachers = teacherRepository.getlevelTeacherByExamType(ExamType.MS.ordinal(),TeacherCourseLevel.COMMON.ordinal());//取出符合级别要求ids
		Specification<Teacher> querySpecific = new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery,
										 CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if(leverTeachers!=null&&!leverTeachers.isEmpty()){
					predicates.add(root.get("id").in(leverTeachers));//id在符合面试类型教师id中
				}else{
					predicates.add(root.get("id").in(0));//没有符合要求的直接添加0
				}
				predicates.add(criteriaBuilder.equal(root.get("teacherType"), TeacherType.JS));//讲师类型
				predicates.add(criteriaBuilder.equal(root.get("status"), 1));//审核状态
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		Page<Teacher> teachers = teacherRepository.findAll(querySpecific, page);
		List<Teacher> content = teachers.getContent();
		List teacherVos=new ArrayList();
		if(content!=null&&!content.isEmpty()){
			for(Teacher teacher:content) {
				TeacherVo teacherVo = new TeacherVo(teacher);
				teacherVos.add(teacherVo);
			}
		}
		PageImpl<TeacherVo> pageTeacheres=new PageImpl(teacherVos,page,teachers == null ? 0 : teachers.getTotalElements());
		return pageTeacheres;
	}

	@Autowired
	private InterviewTeacherRepository interviewTeacherRepository;

	@Override
	public List<Teacher> findInterviewAvailableTeacher(Long courseId,Date date, Integer timeBegin, Integer timeEnd) {
		List<Long> interviewTeachers = interviewTeacherRepository.findTeacherIdByCourseId(courseId);//推荐教师列表
		if(interviewTeachers==null||interviewTeachers.isEmpty()){
			return null;
		}
		List<Long> UnavailableIds = teacherRepository.getUnavailableTeacherIds(date, timeBegin, timeEnd);//已经占用教师id
		UnavailableIds.remove(null);
		Specification<Teacher> querySpecific = new Specification<Teacher>() {
			@Override
			public Predicate toPredicate(Root<Teacher> root, CriteriaQuery<?> criteriaQuery,
										 CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (null!=UnavailableIds&&!UnavailableIds.isEmpty()) {//取出已经占用教师id
					predicates.add(root.get("id").in(UnavailableIds).not());
				}
				if(interviewTeachers!=null&&!interviewTeachers.isEmpty()){//在推荐教师里查找
					predicates.add(root.get("id").in(interviewTeachers));
				}
				predicates.add(criteriaBuilder.equal(root.get("teacherType"), TeacherType.JS));//讲师类型
				predicates.add(criteriaBuilder.equal(root.get("status"), TeacherStatus.ZC));//审核状态
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
		List<Teacher> teachers = teacherRepository.findAll(querySpecific);
		return teachers;
	}

	@Override
	public List<RoleManageVo> getRolesById(Long id) {
		List<RoleManageVo> roleManageVos = Lists.newArrayList();

		teacherRepository.getRolesById(id).forEach(role -> {
			roleManageVos
					.add(new RoleManageVo(Long.parseLong(role[0].toString()), role[1].toString(), role[2] != null));
		});

		return roleManageVos;
	}

	@Override
	@Transactional
	public void updateRolesById(Long id, List<Long> roleIds) {
		// 清空权限
		teacherRepository.clearRolesById(id);

		// 添加权限
		roleIds.forEach(roleId -> {
			teacherRepository.saveRolesById(id, roleId);
		});

	}
}
