package com.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Admin;
import com.model.Building;
import com.model.Logger;
import com.model.Room;
import com.model.Screen;
import com.model.Zone;
import com.service.AdminService;
import com.service.BuildingService;
import com.service.RoomService;
import com.service.ScreenService;
import com.service.ZoneService;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.util.ExportExcel;
import com.util.ImportExcelUtil;
import com.util.JsonUtils;
import com.util.PageUtil;
import com.util.ProduceId;
import com.util.ProduceUsername4;
import com.util.StringRandom;

import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author KONKA
 *
 */
@Controller
@RequestMapping("/screen")
public class ScreenController {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private ScreenService screenService;
	@Autowired
	private ZoneService zoneService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private BuildingService buildingService;
	@Autowired
	private PageUtil pageUtil;
	
	@Value("${defaultPwd}")
	private String defaultPwd;
	/**
     * 查询所有屏幕信息，包括模糊查询，分页
     * @param screen
     * @param modelMap
     * @param index
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("/showAllScreen")
    public String showAllScreen(Screen screen,ModelMap modelMap,@RequestParam(required=true,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request){
    	PageHelper.startPage(index, pageSize);
    	
    	HttpSession session = request.getSession();
    	Admin admin=(Admin) session.getAttribute("admin");
    	Map<String,Object> map=new HashMap<>();
    	if(admin.getPower()==2) {
    		map.put("adminId", admin.getId());
    		screen.setAdminId(admin.getId());
    	}
    	Page<Screen> screenList = null;
    	if(screen.getRoom()!=null) {
    		if(screen.getRoomId()!=null) {
    			screenList = (Page<Screen>) screenService.selectAllScreen(screen);
    		}else if(screen.getRoom().getBuilding().getId()!=null) {
        		map.put("buildingId", screen.getRoom().getBuilding().getId());
        		screenList = (Page<Screen>) screenService.selectScreenByZoneOrBuilding(map);
        	}else if(screen.getRoom().getBuilding().getZone().getId()!=null) {
        		map.put("zoneId", screen.getRoom().getBuilding().getZone().getId());
        		screenList = (Page<Screen>) screenService.selectScreenByZoneOrBuilding(map);
        	}
    	}else {
    		screenList = (Page<Screen>) screenService.selectAllScreen(screen);
    	}
    	
    	/*for (int i = 0; i < screenList.size(); i++) {
    		Screen sc=screenList.get(0);
    		String hour = sc.getDuration();
    		String[] time = hour.split(":");
    		screenList.remove(0);
    		sc.setDuration(time[0]);
    		screenList.add(sc);
		}*/
    	SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
    	/*for (Screen scr : screenList) {
    		String hour = scr.getDuration();
    		String[] time = hour.split(":");
    		screenList.remove(scr);
			scr.setDuration(time[0]);
			screenList.add(scr);
		}*/
    	
    	pageUtil.setPageInfo(screenList, index, pageSize,request);
		modelMap.put("screenList", screenList);
    	List<Zone> zoneList = zoneService.selectAllZone(null);
    	modelMap.put("zoneList", zoneList);
    	return "/screen/list-screen";
    }
    /**
     * 跳转到屏幕的修改页面
     * @param screen
     * @param modelMap
     * @return
     */
    @RequestMapping("/toUpdateScreen")
    public String toUpdateScreen(Screen screen,ModelMap modelMap,HttpSession session){
    	List<Zone> zoneList = zoneService.selectAllZone(null);
    	modelMap.put("zoneList", zoneList);
    	if(screen.getId()!=null){
    		List<Screen> screenList = screenService.selectAllScreen(screen);
    		screen=screenList.get(0);
    		modelMap.put("screen", screen);
    		return "/screen/edit-screen";
    	}else{
    		Admin admin=(Admin) session.getAttribute("admin");
        	if(admin.getPower()==2){
        		int count=screenService.selectAdminLastScreen(admin);
        		modelMap.put("count", count);
        	}
    	}
    	return "/screen/add-screen";
    }
    /**
     * 通过校区获取所在校区的教学楼
     * @param zone
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getZone",produces = "text/json;charset=UTF-8")
    public String getZone(Zone zone){
    	List<Zone> zoneList = zoneService.selectAllZone(zone);
    	zone=zoneList.get(0);
    	return JsonUtils.objectToJson(zone.getBuildingList());
    }
    
    /**
     * 通过教学楼获取所在教学楼内的教室号
     * @param building
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getRoom",produces = "text/json;charset=UTF-8")
    public String getRoom(Building building){
    	Zone zone = zoneService.selectAllRoomByBuilding(building);
    	List<Building> buildingList = zone.getBuildingList();
    	return JsonUtils.objectToJson(buildingList.get(0).getRoomList());
    }
    
    /**
     * 通过选择要新增屏幕的教室生成具体的屏幕
     * @param room 新增屏幕的教室
     * @param num 新增屏幕的数量
     * @param session 暂时存储新增的屏幕，在添加处去掉
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/selectScreenByRoom",produces = "text/json;charset=UTF-8")
    public String selectScreenByRoom(Zone zone,String number,HttpSession session){
    	Admin admin=(Admin) session.getAttribute("admin");
    	JSONObject jsonObject=new JSONObject();
    	if(admin.getScreenRemain()<Integer.parseInt(number)){
    		jsonObject.put("min", "min");
    		return jsonObject.toString();
    	}
    	
    	List<Building> buildingList = zone.getBuildingList();
    	Building building = buildingList.get(0);
    	building.setZoneId(zone.getId());
    	zone = zoneService.selectAllRoomByBuilding(building);
    	Room room=null;
    	
    	
    	List<Screen> screenListOld=new ArrayList<>();
//    	匹配输入的教学楼，如果匹配不到则进行新增操作
    	if(zone==null){
    		session.setAttribute("building", building);
    		List<Room> roomList = building.getRoomList();
    		room = roomList.get(0);
    		
    		session.setAttribute("room", room);
    	}else{
    		buildingList = zone.getBuildingList();
    		for (Building build : buildingList) {
				if(build.getBuildingName().equals(building.getBuildingName())){
					List<Room> roomList = build.getRoomList();
					int count=0;
		    		for (Room r : roomList) {
						if(!r.getNum().equals(building.getRoomList().get(0).getNum())){
							count++;
						}else{
//							教学楼和教室都是已有的
							room=roomService.selectScreenByRoom(r);
							screenListOld=room.getScreenList();
							jsonObject.put("room", room);
						}
					}
//		    		教学楼是已有的，而房间匹配不到，只需新增一个room
		    		if(count==roomList.size()){
		    			room = building.getRoomList().get(0);
		    			room.setBuildingId(buildingList.get(0).getId());
		    			session.setAttribute("room", room);
		    		}
				}
			}
    	}
    	List<Screen> screenListNew=new ArrayList<>();
    	
    	int start=0;
    	if(screenListOld.size()>0){
    		start=screenListOld.size();
    	}
    	List<String> screenIdList = screenService.selectAllId();
    	String newId=null;
    	if(screenIdList.size()==0){
    		newId="sc1";
    	}else{
    		newId=ProduceId.produceUserId(screenIdList);
    	}
    	List<String> usernameList = screenService.selectAllUsername();
    	
    	List<String> usernameNewList=ProduceUsername4.produceScreenUsername(usernameList, Integer.parseInt(number));
    	/*A:while(true) {
    		String usn=StringRandom.getStringRandom(3);
    		int c=0;
    		for (String string : usernameList) {
    			if(usn.equals(string)) {
    				continue;
    			}else {
    				c++;
    			}
    			
    			if(c==usernameList.size()) {
    				usernameNewList.add(usn);
    				usernameList.add(usn);
    				if(usernameNewList.size()==Integer.parseInt(number)) {
    					break A;
    				}
    			}
    		}
    	}*/
    	/*List<String> idList = screenService.selectAllId();
    	List<String> idNewList = ProduceUsername4.produceScreenUsername(idList, Integer.parseInt(number)-start);*/
		for(int i=0;i<Integer.parseInt(number);i++){
			Screen screen=new Screen();
			screen.setId("sc"+Integer.parseInt(usernameNewList.get(i)));
			screen.setTitle("屏幕"+(i+1));
			screen.setUsername(usernameNewList.get(i));
			screen.setPassword(defaultPwd);
			screen.setRoomId(room.getId());
			screenListNew.add(screen);
			String role=newId.substring(0,2);
			String index=newId.substring(2);
			Integer indexNew=(Integer.parseInt(index)+1);
			newId=role+(indexNew.toString());
		}
		
		if(screenListOld.size()>0){
			jsonObject.put("screenListOld", screenListOld);
		}
		jsonObject.put("screenListNew", screenListNew);
		jsonObject.put("room", room);
		return jsonObject.toString();
    }
    
    /**
     * 新增屏幕
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping("/insertScreen")
    public String insertScreen(HttpSession session,Room roomScreen){
    	Building building=(Building) session.getAttribute("building");
    	Room room=(Room) session.getAttribute("room");
    	Admin admin=(Admin) session.getAttribute("admin");
    	if(building!=null){
    		buildingService.insertSelective(building);
    		logger.info(admin.getUsername()+"添加教学楼:"+building.getBuildingName());
    		session.removeAttribute("building");
    		
    		room.setId(UUID.randomUUID().toString());
    		room.setBuildingId(building.getId());
    		roomService.insertSelective(room);
    		logger.info(admin.getUsername()+"添加教室:"+room.getNum());
    		session.removeAttribute("room");
    	}else if(room!=null){
    		/*if(roomScreen.getId().contains(",")) {
    			roomScreen.setId(roomScreen.getId().replace(",", ""));
    		}*/
    		room.setId(UUID.randomUUID().toString());
    		roomService.insertSelective(room);
    		logger.info(admin.getUsername()+"添加教室:"+room.getNum());
    		session.removeAttribute("room");
    	}
    	List<Screen> screenList=roomScreen.getScreenList();
//    	添加和修改的总记录
    	int i=0;
//    	只记录添加数量
    	int insert=0;
    	for (Screen screen : screenList) {
    		Screen screenOld=screenService.selectByPrimaryKey(screen);
    		if(screenOld!=null){
//    			判断用户名重复
    			List<Screen> selectAllScreen = screenService.selectAllScreen(new Screen(screenOld.getUsername()));
    			if(selectAllScreen.size()!=0){
    				if(!selectAllScreen.get(0).getId().equals(screenOld.getId())){
    					return "error";
    				}
    			}
    			if(screenService.updateByPrimaryKeySelective(screen)>0){
    				logger.info(admin.getUsername()+"修改屏幕:"+screen.getId());
    				i++;
    			}
    		}else{
//    			判断用户名重复
    			List<Screen> selectAllScreen = screenService.selectAllScreen(new Screen(screen.getUsername()));
    			if(selectAllScreen.size()!=0){
    				return "error";
    			}
//    			screen
    			screen.setDuration("00:00:00");
        		screen.setNumber(0);
        		screen.setAdminId(admin.getId());
        		if(room!=null) {
        			screen.setRoomId(room.getId());
        		}else {
        			screen.setRoomId(roomScreen.getId());
        		}
        		
        		if(screenService.insertSelective(screen)>0){
        			logger.info(admin.getUsername()+"添加屏幕:"+screen.getUsername());
        			i++;
        			insert++;
        		}
    		}
		}
    	if(i==screenList.size()){
    		session.removeAttribute("screenList");
    		admin.setScreenRemain(admin.getScreenRemain()-insert);
    		session.setAttribute("admin", admin);
    		logger.info(admin.getUsername()+"剩余屏幕数量:"+admin.getScreenRemain());
    		return "success";
    	}
    	return "";
    }
    
    /**
	 * 对比输入的密码和旧密码是否一致
	 * @param password
	 * @return
	 */
    @ResponseBody
    @RequestMapping(value="/testScreenOldPwd")
    public String testScreenOldPwd(Screen screen) {
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
    	List<Screen> screenList = screenService.selectAllScreen(screen);
    	String password = new String(encoder.encode(screen.getPassword().getBytes()));
		password = new String(encoder.encode(password.getBytes()));
    	if(screenList.size()==0) {
    		return "error";
    	}else {
    		if(password.equals(screenList.get(0).getPassword())) {
				return "same";
			}
    	}
    	
    	return "success";
    }
    
    /**
     * 修改屏幕信息
     * @param screen
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateScreen")
    public String updateScreen(Screen screen,HttpSession session){
    	Admin admin=(Admin) session.getAttribute("admin");
    	
    	if(screen.getId()!=null){
    		List<Screen> screenList = screenService.selectAllScreen(null);
    		for (Screen sc : screenList) {
//    			用户名相同且id不同，则认为用户名重复
				if(sc.getUsername().equals(screen.getUsername()) && !sc.getId().equals(screen.getId())){
					logger.info("屏幕用户名存在,修改失败!");
					return "same";
				}
			}
    		if(screenService.updateByPrimaryKeySelective(screen)>0){
    			logger.info(admin.getUsername()+"修改屏幕:"+screen.getId());
    			return "success";
    		}
    	}else{
    		if(screenService.insertSelective(screen)>0){
    			logger.info(admin.getUsername()+"添加屏幕:"+screen.getUsername());
    			return "success";
    		}
    	}
    	return "error";
    }
    /**
     * 删除屏幕信息
     * @param screen
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteScreen")
    public String deleteScreen(Screen screen,HttpSession session){
    	Admin admin=new Admin();
    	screen=screenService.selectByPrimaryKey(screen);
    	admin.setId(screen.getAdminId());
    	admin=adminService.selectByPrimaryKey(admin);
    	try{
    		if(screenService.deleteByPrimaryKey(screen)>0){
//        		将屏幕数量还给其分配的操作管理员
        		admin.setScreenRemain(admin.getScreenRemain()+1);
//        		更新session
        		Admin adminLogin=(Admin) session.getAttribute("admin");
        		
        		logger.info(adminLogin.getUsername()+"删除了屏幕:"+screen.getId());
        		if(adminLogin.getId().equals(admin.getId())){
        			session.setAttribute("admin", admin);
        		}
        		return "success";
        	}
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	return "error";
    }

    /**
     * 跳转到屏幕分配首页
     * @param session
     * @param modelMap
     * @return
     */
    @RequestMapping("/screenDistribute")
    public String screenDistribute(HttpSession session,ModelMap modelMap){
    	Admin admin=(Admin) session.getAttribute("admin");
    	Map<String,Object> map = new HashMap<>();
    	List<Admin> adminYijiList=null;
    	if(admin.getPower()==0){
    		adminYijiList = adminService.selectYijiAdmin(null);
    		for (Admin ad1 : adminYijiList) {
    			map.put("adminId", ad1.getId());
				ad1.setScreenRemain(ad1.getScreenNum()-screenService.selectScreenCount(map));
				List<Admin> adminList = ad1.getAdminList();
				for (Admin ad2 : adminList) {
					map.put("adminId", ad2.getId());
					ad2.setScreenRemain(ad2.getScreenNum()-screenService.selectScreenCount(map));
				}
			}
    		modelMap.put("adminYijiList", adminYijiList);
    	}else if(admin.getPower()==1){
    		map.put("adminId1", admin.getId());
    		adminYijiList = adminService.selectYijiAdmin(map);
    		admin=adminYijiList.get(0);
    		map.put("adminId", admin.getId());
    		admin.setScreenRemain(admin.getScreenNum()-screenService.selectScreenCount(map));
    		List<Admin> adminList = admin.getAdminList();
			for (Admin ad2 : adminList) {
				map.put("adminId", ad2.getId());
				ad2.setScreenRemain(ad2.getScreenNum()-screenService.selectScreenCount(map));
			}
    	}
    	modelMap.put("admin", admin);
    	return "/screen/screenDistribute";
    }
    /**
     * excel批量上传screen信息
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/uploadScreenExcel",method={RequestMethod.GET,RequestMethod.POST})
    public String uploadScreenExcel(HttpServletRequest request)throws Exception {
    	 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
         System.out.println("通过传统方式form表单提交方式导入excel文件！");  
           
         InputStream in =null;  
         List<List<Object>> listob = null;  
         MultipartFile file = multipartRequest.getFile("upfile");  
         if(file.isEmpty()){  
             throw new Exception("文件不存在！");  
         }  
         in = file.getInputStream();  
         listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());  
         in.close();  
           
         //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出  
         List<Integer> resultList=new ArrayList<>(listob.size());
         
         for (int i = 0; i < listob.size(); i++) {  
             List<Object> lo = listob.get(i); 
             Screen screen=new Screen();
             String id=ProduceId.produceUserId(screenService.selectAllId());
             screen.setId(id);
             screen.setUsername(String.valueOf(lo.get(0)));
             screen.setPassword(String.valueOf(lo.get(1)));
             screen.setRoomId(String.valueOf(lo.get(2)));
             resultList.add(screenService.insertSelective(screen));
             /*InfoVo vo = new InfoVo();  
             vo.setCode(String.valueOf(lo.get(0)));  */
             System.out.println(screen);
         }  
         String result="";
         int i=0;
         for (Integer integer : resultList) {
 			if(integer==1){
 				i++;
 			}
 		}
         if(i==0){
         	result+="导入失败！";
         }else{
         	result+="共"+resultList.size()+"条信息，成功导入"+i+"条信息，导入失败"+(resultList.size()-i)+"条信息";
         }
         logger.info(result);
    	return "redirect:showAllScreen";
    }
    
    /**
     * 屏幕信息导出Excel表格
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response,HttpSession session) throws Exception {
    	Admin admin=(Admin) session.getAttribute("admin");
    	
        // 定义表的标题
        String title = "屏幕信息";
        
        //定义表的列名
        String[] rowsName = new String[] { "校区", "教学楼", "教室", "ID号", "屏幕登录名" ,"屏幕类型"};
        
        //定义表的内容
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
//        List<Person> listPerson = ps.listPerson();
        List<Screen> screenList = screenService.selectAllScreen(null);
        for (int i = 0; i < screenList.size(); i++) {
        	Screen sc = screenList.get(i);
            objs = new Object[rowsName.length];
            objs[0] = sc.getRoom().getBuilding().getZone().getZoneName();
            objs[1] = sc.getRoom().getBuilding().getBuildingName();
            objs[2] = sc.getRoom().getNum();
            objs[3] = sc.getId();
            objs[4] = sc.getUsername();
            if(sc.getType().equals("1")) {
            	objs[5]="触摸屏";
            }else if(sc.getType().equals("2")){
            	objs[5]="文档屏";
            }else if(sc.getType().equals("3")){
            	objs[5]="投影";
            }else if(sc.getType().equals("4")){
            	objs[5]="电视";
            }else if(sc.getType().equals("5")){
            	objs[5]="临时屏幕";
            }
            dataList.add(objs);
        }
        
        // 创建ExportExcel对象
        ExportExcel ex = new ExportExcel(title, rowsName, dataList);
        logger.info(admin.getUsername()+"导出屏幕excel表格");
        // 输出Excel文件
        try {
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition",
                    "attachment; filename=screenInfo.xls");
            response.setContentType("application/msexcel");
            ex.export(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    
    /**
     * 屏幕转移功能
     * @param screen
     * @param modelMap
     * @return
     */
    @RequestMapping("/screenTransfer")
    public String screenTransfer(Screen screen,ModelMap modelMap){
    	List<Screen> screenList = screenService.selectAllScreen(screen);
    	screen=screenList.get(0);
    	modelMap.put("screen", screen);
    	
    	List<Zone> zoneList = zoneService.selectAllZone(null);
    	modelMap.put("zoneList", zoneList);
    	return "/screen/screen-transfer";
    }
}
