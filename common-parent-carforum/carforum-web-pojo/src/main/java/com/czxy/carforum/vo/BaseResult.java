package com.czxy.carforum.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/3/1.
 */
@Data
public class BaseResult {

    private Map<String, Object> data = new HashMap<>();

    public BaseResult(Integer errno, String errmsg) {
        data.put("errno", errno);
        data.put("errmsg", errmsg);
    }

    /**
     * 追加其他参数，支持链式
     *
     * @param key
     * @param msg
     * @return
     */
    public BaseResult append(String key, Object msg) {
        data.put(key, msg);
        return this;
    }
}
