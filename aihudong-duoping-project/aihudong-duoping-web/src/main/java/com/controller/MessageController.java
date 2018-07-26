package com.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Admin;
import com.model.Building;
import com.model.Logger;
import com.model.Message;
import com.model.Room;
import com.model.Zone;
import com.service.MessageService;
import com.service.RoomService;
import com.service.ZoneService;
import com.util.JsonUtils;
import com.util.PageUtil;

@Controller
@RequestMapping("/message")
public class MessageController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private MessageService messageService;
	@Autowired
	private ZoneService zoneService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private PageUtil pageUtil;
	
	/**
	 * 查询所有消息
	 * @param message
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping("/showAllMessage")
	public String showAllMessage(Message message,ModelMap modelMap,@RequestParam(required=true,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request,HttpSession session) {
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		
		PageHelper.startPage(index, pageSize);
		
		Map<String,Object> map=new HashMap<>();
		
		if(message.getMessageName()!=null) {
			map.put("messageName", message.getMessageName());
		}
		
		Page<Message> messageList = (Page<Message>) messageService.selectAllMessage(map);
		for (Message mess : messageList) {
			String[] startSplit=sdf.format(mess.getStartTime()).split(" ");
			String[] endSplit=sdf.format(mess.getEndTime()).split(" ");
			if(mess.getStartTime().getTime()>new Date().getTime()) {
				mess.setMessageState(0);
			}else if(mess.getStartTime().getTime()<new Date().getTime() && mess.getEndTime().getTime()>new Date().getTime()) {
				mess.setMessageState(1);
			}else if(mess.getEndTime().getTime()<new Date().getTime()) {
				mess.setMessageState(2);
			}
			mess.setStartTimeString(startSplit[0]+"-"+endSplit[0]);
			mess.setEndTimeString(startSplit[1]+"-"+endSplit[1]);
		}
		pageUtil.setPageInfo(messageList, index, pageSize,request);
		
		modelMap.put("messageList", messageList);
		
		List<Zone> zoneList = zoneService.selectAllZone(null);
		modelMap.put("zoneList", zoneList);
		
		return "/message/list-message";
	}
	
	/**
	 * 跳转到添加、修改页面
	 * @param message
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toUpdateMessage")
	public String toUpdateMessage(Message message,ModelMap modelMap) {
		//查询所有消息
		Map<String,Object> map=new HashMap<>();
		List<Message> messageList = messageService.selectAllMessage(map);
		
		if(message.getId()!=null) {
			map.put("id", message.getId());
			for (int i = 0; i < messageList.size(); i++) {
				if(messageList.get(i).getId().equals(message.getId())) {
					modelMap.put("message", messageList.get(i));
					messageList.remove(i);
				}
			}
			
		}
		List<Zone> zoneList = zoneService.selectAllZone(null);
		
		for (Message mess : messageList) {
			//不考虑过期的消息
			if(mess.getEndTime().getTime()<new Date().getTime()) {
				continue;
			}
			//移除已经推送消息的房间
			if (!mess.getId().equals(message.getId())) {
				// 遍历校区、教学楼至教室
				for (int j = 0;j < zoneList.size();j++) {
					List<Building> buildingList = zoneList.get(j).getBuildingList();
					for (int q = 0;q < buildingList.size();q++) {
						List<Room> roomList = buildingList.get(q).getRoomList();
						for (int i = 0; i < roomList.size(); i++) {
							if (mess.getRoomId().contains(roomList.get(i).getId())) {
								roomList.remove(i);
								i--;
							}
						}
						buildingList.get(q).setRoomList(roomList);
						if(buildingList.get(q).getRoomList().size()==0) {
							buildingList.remove(q);
							q--;
						}
					}
					zoneList.get(j).setBuildingList(buildingList);
					if(zoneList.get(j).getBuildingList().size()==0) {
						zoneList.remove(j);
						j--;
					}
				}
			}
		}
		/*modelMap.put("roomIdString", roomIdString);
		modelMap.put("buildingIdString", buildingIdString);
		modelMap.put("zoneIdString", zoneIdString);*/
		
		modelMap.put("zoneList", zoneList);
		return "/message/edit-message";
	}
	
	/**
	 * 新增、修改消息
	 * @param message 消息实体类
	 * @param startTimeString 开始时间
 	 * @param endTimeString 结束时间
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/updateMessage")
	public String updateMessage(Message message,@RequestParam(required=false) String startTimeString,
			@RequestParam(required=false) String endTimeString,HttpServletRequest request,HttpSession session,
			ModelMap modelMap) throws ParseException {
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//设置推送的开始和结束时间
		if(startTimeString==null||endTimeString==null) {
			modelMap.put("error", "推送时间不能为空!");
			return "/error/404";
		}
		if(sdf.parse(startTimeString).getTime()>=sdf.parse(endTimeString).getTime()) {
			modelMap.put("error", "开始时间不得小于结束时间!");
			return "/error/404";
		}
		message.setStartTime(sdf.parse(startTimeString));
		message.setEndTime(sdf.parse(endTimeString));
		
		//设置校区id字符串，中间用逗号隔开
		message.setZoneId(message.getZoneList().get(0).getZoneName());
		//设置教学楼id字符串，中间用逗号隔开
		message.setBuildingId(message.getBuildingList().get(0).getBuildingName());
		//设置房间id字符串，中间用逗号隔开
		message.setRoomId(message.getRoomList().get(0).getId());
		
		//设置推送图片名称字符串，中间用逗号隔开
		String picString="";
		List<MultipartFile> fileList = message.getFileList();
		if(fileList.get(0).getSize()!=0) {
			for (MultipartFile file : fileList) {
				String fileName = file.getOriginalFilename();
				String realPath = request.getServletContext().getRealPath("/upload");
				String newFileName=UUID.randomUUID()+fileName.substring(fileName.lastIndexOf("."));
				String filePath = realPath+File.separator+newFileName;
				File uploadFile =new File(filePath);
				try {
					file.transferTo(uploadFile);
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(fileList.get(0).getOriginalFilename().equals(file.getOriginalFilename())) {
					picString+=newFileName;
				}else {
					picString+=","+newFileName;
				}
			}
			message.setMessagePic(picString);
		}
		
		//设置推送消息的管理员id
		message.setAdminId(((Admin)session.getAttribute("admin")).getId().toString());
		
		try {
			if(message.getId()!=null) {
				if(messageService.updateByPrimaryKeySelective(message)>0) {
					return "redirect:/message/showAllMessage";
				}
			}else {
				if(messageService.insertSelective(message)>0) {
					return "redirect:/message/showAllMessage";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "/error/404";
		}
		return "/error/404";
	}
	
	/**
	 * 删除消息
	 * 
	 * @param message
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteMessage")
	public String deleteMessage(Message message) {
		if(messageService.deleteByPrimaryKey(message)>0) {
			return "success";
		}
		return "";
	}
	
	/**
	 * 查询消息的接收终端
	 * @param message
	 * @param modelMap
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/showContent",produces = "text/json;charset=UTF-8")
	public String showContent(Message message) {
		message=messageService.selectByPrimaryKey(message);
		String[] roomIdList = message.getRoomId().split(",");
		List<Room> roomList=new ArrayList<>();
		for (String roomId : roomIdList) {
			Map<String,Object> map=new HashMap<>();
			map.put("roomId", roomId);
			roomList.add(roomService.selectRoomBuildZone(map));
		}
		
		return JsonUtils.objectToJson(roomList);
	}
	
	/**
	 * 查询消息的图片
	 * @param message
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/showPic")
	public String showPic(Message message,HttpServletRequest request) {
		message=messageService.selectByPrimaryKey(message);
		String[] picStringList = message.getMessagePic().split(",");
		
		List<String> picList=new ArrayList<>();
		for (String picString : picStringList) {
			picString=request.getScheme() + "://"
					+ request.getServerName() + ":"
					+ request.getServerPort()
					+ "/aihudong-web/upload/"
					+picString;
			
			picList.add(picString);
		}
		
		return JsonUtils.objectToJson(picList);
	}
}
