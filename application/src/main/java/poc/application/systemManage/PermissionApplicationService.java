package poc.application.systemManage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import poc.domain.systemManage.model.PermissionSource;
import poc.domain.systemManage.repository.PermissionRepository;

import java.util.*;

@Service
public class PermissionApplicationService {
    private final PermissionRepository repository;


    public PermissionApplicationService(PermissionRepository repository) {
        this.repository = repository;
    }

    public List<PermissionSource> queryAllMenu() {
        return repository.queryAllMenu();
    }
    public String getAllPermissionByUserId(String userId){
        return repository.getAllPermissionByUserId(userId);
    }

    public Object getMenuJson(String permissions,String permissionType){
        Map<String,Object> m = new HashMap<>();
        if (permissions==null||permissions.equals("")){
            m.put("mainList","");
            return JSONObject.toJSON(m);
        }
        List<String> permissionList = Arrays.asList(permissions.split(","));
        List<PermissionSource> permissionSources = repository.getOneMenu(permissionType);
        List<Map<String,Object>> oneList = new ArrayList<>();
        //一级菜单
        for (PermissionSource p :permissionSources){
            Map<String,Object> map = new HashMap<>();
            map.put("title",p.getPermissionName());
            map.put("icon",p.getIcon());
            map.put("url",p.getPermissionUrl());
            map.put("hidden",!permissionList.contains(p.getPermissionCode()));
            //二级菜单
            List<PermissionSource> permissionSources2 = repository.queryChildrenMenu(p.getPermissionId());
            List<Map<String,Object>> twoList = new ArrayList<>();
            int i = 1;
            for (PermissionSource p2:permissionSources2){
                Map<String,Object> map2 = new HashMap<>();
                map2.put("id",i+"");
                i++;
                map2.put("title",p2.getPermissionName());
                map2.put("icon",p2.getIcon());
                map2.put("url",p2.getPermissionUrl());
                map2.put("hidden",!permissionList.contains(p2.getPermissionCode()));
                map2.put("haschild",permissionList.contains(p2.getPermissionCode()));
                List<PermissionSource> permissionSources3 = repository.queryChildrenMenu(p2.getPermissionId());
                if (permissionSources3!=null&&permissionSources3.size()<1){
                    map2.put("haschild",false);
                }
                List<Map<String,Object>> threeList = new ArrayList<>();
                int j = 1;
                for(PermissionSource p3:permissionSources3){
                    Map<String,Object> map3 = new HashMap<>();
                    map3.put("id",i+"-"+j);
                    j++;
                    map3.put("title",p3.getPermissionName());
                    map3.put("icon",p3.getIcon());
                    map3.put("url",p3.getPermissionUrl());
                    map3.put("hidden",!permissionList.contains(p3.getPermissionCode()));
                    map3.put("haschild",false);
                    threeList.add(map3);
                }
                map2.put("children",threeList);
                twoList.add(map2);
            }
            map.put("children",twoList);
            oneList.add(map);
        }

        m.put("mainList",oneList);
        return JSONObject.toJSON(m);
    }
}
