package com.alnoor.modelacademy;

import android.util.Base64;
import org.json.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class FirebaseRest {
    public interface Callback { void done(boolean ok, String data); }
    private final String apiKey, dbUrl;
    private String idToken, uid;
    public FirebaseRest(String apiKey, String dbUrl){ this.apiKey=apiKey; this.dbUrl=dbUrl.replaceAll("/$",""); }
    public boolean configured(){ return !apiKey.startsWith("YOUR_") && !dbUrl.contains("YOUR_PROJECT_ID"); }
    public String getUid(){return uid;} public String getToken(){return idToken;}
    public void login(String email,String password,Callback cb){
        new Thread(() -> { try {
            JSONObject body=new JSONObject().put("email",email).put("password",password).put("returnSecureToken",true);
            String s=post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="+URLEncoder.encode(apiKey,"UTF-8"),body.toString(),null);
            JSONObject j=new JSONObject(s); idToken=j.getString("idToken"); uid=j.getString("localId"); cb.done(true,s);
        } catch(Exception e){cb.done(false,e.getMessage());}}).start();
    }
    public void get(String path,Callback cb){ request("GET",path,null,cb); }
    public void put(String path,JSONObject data,Callback cb){ request("PUT",path,data.toString(),cb); }
    public void patch(String path,JSONObject data,Callback cb){ request("PATCH",path,data.toString(),cb); }
    private void request(String method,String path,String body,Callback cb){ new Thread(() -> { try { String url=dbUrl+"/"+path.replaceAll("^/","")+".json"; if(idToken!=null) url += "?auth="+URLEncoder.encode(idToken,"UTF-8"); String out=raw(method,url,body); cb.done(true,out); } catch(Exception e){cb.done(false,e.getMessage());}}).start(); }
    private String post(String url,String body,String token)throws Exception{return raw("POST",url,body);}
    private String raw(String method,String url,String body)throws Exception{
        HttpURLConnection c=(HttpURLConnection)new URL(url).openConnection(); c.setRequestMethod(method); c.setConnectTimeout(15000); c.setReadTimeout(20000); c.setRequestProperty("Content-Type","application/json");
        if(body!=null){c.setDoOutput(true);try(OutputStream os=c.getOutputStream()){os.write(body.getBytes(StandardCharsets.UTF_8));}}
        int code=c.getResponseCode(); InputStream is=code>=200&&code<300?c.getInputStream():c.getErrorStream(); StringBuilder sb=new StringBuilder(); try(BufferedReader r=new BufferedReader(new InputStreamReader(is,StandardCharsets.UTF_8))){String line;while((line=r.readLine())!=null)sb.append(line);} if(code<200||code>=300) throw new IOException(sb.toString()); return sb.toString();
    }
    public static JSONObject userFrom(String json){try{return new JSONObject(json);}catch(Exception e){return new JSONObject();}}
}
