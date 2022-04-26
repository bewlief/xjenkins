package com.figaw

/**
 * 展示了静态函数的使用
 */
class SomeStaticHelper {

    static def someStaticValue = 21

    static def aStaticHelperFunction() {
        "this is some static value: " + someStaticValue
    }

}
