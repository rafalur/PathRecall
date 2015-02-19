package com.rafal.pathrecall.ui.utils;

public class LinearFunctionParameters {
    public float a;
    public float b;

    private LinearFunctionParameters(float _a, float _b){
        a = _a;
        b = _b;
    }

    public static LinearFunctionParameters getParametersFromTwoPoints(float startX, float startY, float endX, float endY){
        float a = (endY - startY)/(endX - startX);
        float b;

        if(endX == startX) {
            b = startY;
        }
        else{
            b = (endX*startY - startX*endY) / (endX - startX);
        }

        return new LinearFunctionParameters(a, b);
    }
}
