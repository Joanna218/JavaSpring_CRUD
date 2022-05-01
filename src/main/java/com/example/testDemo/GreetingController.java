package com.example.testDemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
public class GreetingController {
    @Value("${greeting-name: Mirage}")
    private String name;

    @Value("${greeting-coffee: ${greeting-name} is drinking Cafe  Ganador ! }")
    private String coffee;

    @GetMapping("/name")
    public String getName() {
        return name;
    }

    @GetMapping("/coffee")
    public String getCoffee() {
        return coffee;
    }

    /*
    * 有掛 @Value("${ 全域設定的名稱 : 若無全域設定的名稱所帶入的預設值 }") 的 annotation，此屬性會先去找全域設定的組態有沒有設定相對應的名稱
    * 若有則套用全域設定，若沒有則套用此屬性給的預設值
    * 順序為 都先去找全域的設定值
    *
    * 若使用有關聯性的 @Value時會有限制，只要第二層的設定值在全域找不到，他沒辦法自動辨識出第二層的預設值
    * 情況為：當全域的第一層使用預設值後，此預設值又有牽連到另一個變數值，此時把第二個全域的值也註解掉，他會找不到而爆錯誤
    * */
}
