package com.example.chm;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface MyInterface4 {

    @LambdaFunction
    Response4Class DeleteFood2(Request4Class request);
}
