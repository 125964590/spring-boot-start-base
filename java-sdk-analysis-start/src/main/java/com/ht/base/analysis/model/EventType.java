package com.ht.base.analysis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhengyi
 * @date 2018-12-24 10:47
 **/
@AllArgsConstructor
public enum EventType {

    /**
     * event type enum
     */
    HuaTuOnline_app_HuaTuOnline_SearchPlease,
    HuaTuOnline_app_pc_HuaTuOnline_CollectTest,
    HuaTuOnline_pc_HuaTuOnline_SearchPlease,
    HuaTuOnline_app_pc_HuaTuOnline_ShareTest;

    public String getType(EventType eventType) {
        return eventType.toString();
    }

    @AllArgsConstructor
    public enum ShareTest implements BaseTemplate {
        /**
         * share key enum
         */
        on_module("on_module"),
        test_id("test_id"),
        test_first_cate("test_first_cate"),
        test_second_cate("test_second_cate"),
        test_third_cate("test_third_cate"),
        test_correct_rate("test_correct_rate"),
        test_answer_duration("test_answer_duration"),
        test_easy_error_choiceEasyErrorChoice("test_easy_error_choiceEasyErrorChoice"),
        test_from("test_from"),
        test_type("test_type"),
        share_type("share_type");

        private String key;

        public String getKey() {
            return this.key;
        }
    }

    @AllArgsConstructor
    public enum SearchPlease implements BaseTemplate {
        /**
         * search key enum
         */
        first_module("first_module"),
        second_module("second_module"),
        search_keyword("search_keyword"),
        is_recommend_word_used("is_recommend_word_used"),
        is_history_word_used("is_history_word_used");

        private String key;

        public String getKey() {
            return this.key;
        }
    }

    @AllArgsConstructor
    public enum CollectTest implements BaseTemplate {
        /**
         * collect test key enum
         */
        on_module("on_module"),
        collect_operation("collect_operation"),
        test_id("test_id"),
        test_first_cate("test_first_cate"),
        test_second_cate("test_second_cate"),
        test_third_cate("test_third_cate");

        private String key;

        public String getKey() {
            return this.key;
        }
    }

    @AllArgsConstructor
    public enum Default implements BaseTemplate {

        /**
         * default key
         */
        platform("platform");
        private String key;

        public String getKey() {
            return this.key;
        }
    }

    @AllArgsConstructor
    @Getter
    public enum TerminalType {
        /**
         * terminal type
         */
        andoraid(1, "AndoraidApp"),
        ios(2, "iosApp"),
        pc(3, "PC"),
        andora_ipad(4, "AndoraidApp"),
        ios_ipad(5, "iosApp"),
        wx(6, "wx"),
        m_pc(7, "PC"),
        batch_register(8, "batch"),
        education(9, "education"),
        little_program(21, "小程序");
        private int terminal;
        private String terminalName;

        public static String getTerminalName(int terminal) {
            for (TerminalType value : TerminalType.values()) {
                if (value.getTerminal() == terminal) {
                    return value.getTerminalName();
                }
            }
            return "无终端类型";
        }
    }

}