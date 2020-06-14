package com.example.chm;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

public interface MyInterface {

    @LambdaFunction
    ResponseClass diet_rd_n_cal(RequestClass request);
}
