package com.example.chm;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface MyInterface3 {

    @LambdaFunction
    Response3Class getaudiofood(Request3Class request);
}
