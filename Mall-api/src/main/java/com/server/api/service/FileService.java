package com.server.api.service;


import com.android.zcomponent.http.api.host.Endpoint;
import com.android.zcomponent.json.WebFormEncoder;
import com.android.zcomponent.communication.http.Context.Method;
import com.android.zcomponent.communication.http.annotation.HttpHost;
import com.android.zcomponent.communication.http.annotation.HttpPortal;


public class FileService
{

    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/File/uploadPicture", method = Method.Post, encoder = WebFormEncoder.class)
    @HttpHost(headers = {})
    public static class PostFileRequest extends Endpoint
    {

    }
    
    @HttpPortal(path = "http://jzb.jxlnxx.com/?s=/Api/File/uploadMovie", method = Method.Post, encoder = WebFormEncoder.class)
    @HttpHost(headers = {})
    public static class PostFileVideoRequest extends Endpoint
    {

    }
}
