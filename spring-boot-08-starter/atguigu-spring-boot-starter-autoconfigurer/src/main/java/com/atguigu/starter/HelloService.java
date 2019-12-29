package com.atguigu.starter;

import org.springframework.beans.factory.annotation.Autowired;

public class HelloService {


    HelloProperties helloProperties;

    public HelloProperties getHelloProperties() {
        return helloProperties;
    }

    public void setHelloProperties(HelloProperties helloProperties) {
        this.helloProperties = helloProperties;
    }

    public String SayHelloAtguigu(String name){
        return helloProperties.getPrefix()+"-"+ name+"-"+helloProperties.getSuffix();
    }
}
