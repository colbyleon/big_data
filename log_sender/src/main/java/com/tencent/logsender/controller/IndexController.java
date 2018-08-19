package com.tencent.logsender.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/A/{data}")
    public String  index(Model model, @PathVariable String data) {
        model.addAttribute("data", data);
        return "index";
    }

    @RequestMapping("/B/{data}")
    public String index2(Model model, @PathVariable String data) {
        model.addAttribute("data", data);
        return "index2";
    }
}
