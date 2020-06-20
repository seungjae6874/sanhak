package com.example.chm;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface MyInterface2 {

    @LambdaFunction
    Response2Class getFoodinfo(Request2Class request);
}
