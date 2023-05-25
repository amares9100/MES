package mes.controller.material;

import lombok.extern.slf4j.Slf4j;
import mes.domain.dto.material.InOutPageDto;
import mes.domain.dto.material.MaterialInOutDto;
import mes.service.Material.MaterialInoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;

@RestController
@Slf4j
@RequestMapping("/materialInout")
public class MaterialInoutController {

    @Autowired
    private MaterialInoutService materialInoutService;

    // 재고 생성
    @PostMapping("/materialIn")
    public boolean materialIn(@RequestBody MaterialInOutDto dto){
        System.out.println("materialIn : " + dto);


        return materialInoutService.materialIn(dto);
    }
    // 재고 생성 리스트 불러오기
    @GetMapping("/MaterialInOutList")
    public InOutPageDto MaterialInOutList(InOutPageDto dto) throws ParseException {


        return materialInoutService.MaterialInOutList(dto);
    }
    // 재고 최종승인
    @PutMapping("/MaterialStock")
    public boolean MaterialInStock(@RequestBody MaterialInOutDto dto){


        return materialInoutService.MaterialInStock(dto);
    }
    // 재고 증가 취소
    @DeleteMapping("/MaterialDelete")
    public boolean MaterialDelete(@RequestParam int mat_in_outid){

        return materialInoutService.MaterialDelete(mat_in_outid);
    }

}
