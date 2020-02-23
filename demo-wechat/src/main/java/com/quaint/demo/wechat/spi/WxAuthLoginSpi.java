package com.quaint.demo.wechat.spi;

import com.quaint.demo.wechat.dto.DemoWxAuthLogin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author quaint
 * @date 15 February 2020
 * @since 1.30
 */
@RestController
public class WxAuthLoginSpi {


    @PostMapping("/wx/silent/login")
    String wxSilentLogin(DemoWxAuthLogin.Param param){
        return null;
    }


}
