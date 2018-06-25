package com.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Component
public class bbbApi {
	
	@Value("${httpUrl}")
	private static String httpUrl;
	/*@Value("${salt}")
	private static String salt="c5ee9cde31f25f6ba7119361b8d16498";*/
	
	public static String BigBlueButtonURL = httpUrl+"/bigbluebutton/api/";
	
	public static String createMeeting(String meetingID,String salt) {
		Integer n = 70000 + RandomUtils.nextInt(9999);
		String create_parameters = "name="+meetingID+"&meetingID="+meetingID+"&voiceBridge="+n+"&attendeePW=ap&moderatorPW=mp&isBreakoutRoom=false&record=false";
		String enterURL = BigBlueButtonURL + "create?"+ create_parameters+"&checksum="+checkSum("create" + create_parameters + salt);
		System.out.println("api--->createMeeting");
		//System.out.println(enterURL);
		return create_parameters+"&checksum="+checkSum("create" + create_parameters + salt);
	}
	
	public static String joinMeeting(String meetingID,String username,String salt) {
		
		BASE64Encoder encoder = new BASE64Encoder();
		BASE64Decoder decoder = new BASE64Decoder();
		
		String join_parameters = "meetingID="+meetingID+"&joinViaHtml5=true&password=mp&fullName="+username;
		String joinURL = BigBlueButtonURL + "join?"+ join_parameters+"&checksum="+checkSum("join" + join_parameters + salt);
		System.out.println("api--->joinMeeting");
		//System.out.println(joinURL);
		return join_parameters+"&checksum="+checkSum("join" + join_parameters + salt);
	}
	
	public static String getMeetingInfo(String meetingID,String salt) {
		String parameters = "meetingID="+meetingID;
		String getURL = BigBlueButtonURL + "getMeetingInfo?"+ parameters+"&checksum="+checkSum("getMeetingInfo" + parameters + salt);
		System.out.println("api--->getMeetingInfo");
		//System.out.println(getURL);
		return parameters+"&checksum="+checkSum("getMeetingInfo" + parameters + salt);
	}
	
	public static String endMeeting (String meetingID,String salt) {
		String end_parameters = "meetingID=" +meetingID+"&password=mp";
		 
		String sum=checkSum("end" + end_parameters + salt);
		String enterURL = BigBlueButtonURL + "end?"+ end_parameters+"&checksum="+sum;
		System.out.println("api--->endMeeting");
		//System.out.println(enterURL);
		/*其中：
		Checksum生成java代码函数：*/
		return end_parameters+"&checksum="+checkSum("end" + end_parameters + salt);
	}
	
	
	public static String isMeetingRunning(String meetingID,String salt) {
		String create_parameters = "meetingID=" +meetingID;
		 
		String sum=checkSum("isMeetingRunning" + create_parameters + salt);
		String enterURL = BigBlueButtonURL + "isMeetingRunning?"+ create_parameters+"&checksum="+sum;
		System.out.println("api--->isMeetingRunning");
		//System.out.println(enterURL);
		return create_parameters+"&checksum="+sum;
	}
	
	public static String checkSum(String s) {
        String checksum = "";
        try {
                  checksum = DigestUtils.shaHex(s);
        } catch (Exception e) {
                  e.printStackTrace();
        }
        return checksum;
	}
}
