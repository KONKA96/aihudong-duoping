package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Building;
import com.service.BuildingService;

@Controller
@RequestMapping("/building")
public class BuildingController {
	
	@Autowired
	private BuildingService buildingService;
	
	/**
	 * 删除或修改教学楼的负责人
	 * @param building
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateBuildingAdmin")
	public String updateBuildingAdmin(Building building){
		List<Building> buildingList = buildingService.selectAllBuilding(building);
		Building buildingOld =buildingList.get(0);
		if(building.getAdminId()==null){
			buildingOld.setAdminId(0);
			if(buildingService.updateByPrimaryKeySelective(buildingOld)>0){
				return "success";
			}
		}else{
			if(buildingList.size()<=0){
				return "none";
			}else{
				building.setId(buildingOld.getId());
			}
			if(buildingOld.getAdminId()!=building.getAdminId() && buildingOld.getAdminId()!=null && buildingOld.getAdminId()!=0){
				return "error";
			}
			buildingOld.setAdminId(building.getAdminId());
			if(buildingService.updateByPrimaryKeySelective(buildingOld)>0){
				return "success";
			}
		}
		
		return "";
	}
	/**
	 * 修改和添加教学楼
	 * @param building
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateBuilding")
	public String updateBuilding(Building building){
		if(building.getId()!=null){
			if(buildingService.updateByPrimaryKeySelective(building)>0){
				return "success";
			}
		}else{
			List<Building> buildingList = buildingService.selectAllBuilding(building);
			if(buildingList.size()>0){
				return "error";
			}
			if(buildingService.insertSelective(building)>0){
				return "success";
			}
		}
		return "";
	}

}
