package mes.controller.material;


import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.MaterialDto;
import mes.domain.dto.material.MaterialPageDto;
import mes.domain.dto.member.CompanyDto;
import mes.service.Material.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;


    // 자재 입고(생성)
    @PostMapping("/materialCreate")
    public boolean materialCreate(@RequestBody MaterialDto dto){
        System.out.println("dto" + dto);

        return materialService.materialCreate(dto);
    }

    // 자재 리스트
    @GetMapping("/materialList")
    public MaterialPageDto materialList(MaterialPageDto dto){


        return materialService.materialList(dto);
    }

    // 회사 불러오기
    @GetMapping("/getcompany")
    public List<CompanyDto> getCompany(){
        System.out.printf("getCompany");
        List<CompanyDto> list = materialService.getCompany();
        System.out.println(list);
        return list;
    }
    // 자재 수정
    @PutMapping("/materialUpdate")
    public boolean materialUpdate(@RequestBody MaterialDto dto){
        return materialService.materialUpdate(dto);
    }

}
