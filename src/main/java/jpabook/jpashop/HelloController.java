package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "Helloπ");

        // resource/template νμ ννλ¦Ώ νμΌλͺ return (νμΌ νμ₯μ μλ΅)
        return "hello";
    }
}
