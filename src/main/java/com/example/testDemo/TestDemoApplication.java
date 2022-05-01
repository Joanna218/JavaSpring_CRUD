package com.example.testDemo;

import jdk.jfr.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestDemoApplication.class, args);
	}

}
// 為了使 Controller 功能單一化，將初始化資料樣本提高層次為單一元件
@Component
class DataLoader {
	private final CoffeeRepository coffeeRepository;

	@Autowired
	public DataLoader(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}

	@PostConstruct
	private void loadData() {
		// 步驟四：建立咖啡資料
		coffeeRepository.saveAll(List.of(
				new Coffee("Café Cereza"),
				new Coffee("Café Ganador"),
				new Coffee("Café Lareño"),
				new Coffee("Café Três Pontas")
		));
	}
}

@RestController
@RequestMapping("/coffees")
class RestApiDemoController {

	// 步驟三：注入 CoffeeRepository，並建立 constructor
	private final CoffeeRepository coffeeRepository;

	@Autowired
	public RestApiDemoController(CoffeeRepository coffeeRepository) {
		this.coffeeRepository = coffeeRepository;
	}

	// 步驟五：逐一修改為 coffeeRepository，並且使用 CrudRepository 中寫好的方法

	// 得到全部的咖啡資料
	@GetMapping
	Iterable<Coffee> getCoffees() {
		return coffeeRepository.findAll();
	}

	// 得到某一筆的咖啡資料，傳入 id 查詢
	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		return coffeeRepository.findById(id);
	}

	@PostMapping
	Coffee postCoffee(@RequestBody Coffee coffee) {
		return coffeeRepository.save(coffee);
	}

	// 若無此更新資料就新增一筆，若有此資料就更新
	@PutMapping("/{id}")
	ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		// 存在 => 更新(HttpStatus.OK)，不存在 => 新增(CREATED)
		return (coffeeRepository.existsById(id)) ?
				new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK) :
				new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED);
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
		coffeeRepository.deleteById(id);
	}
}

// 步驟二：新增 CoffeeRepository，去繼承 CrudRepository 中已經寫好的方法
interface CoffeeRepository extends CrudRepository<Coffee, String> {}

// 步驟一：將 class Model 注釋為 @Entity，並建構空的建構子
@Entity // 是 JPA 的 annotations 注釋
class Coffee {
	@Id
	private  String id;
	private String name;

	// 使用 Java Persistence API 會要求使用一個無參數的建構子
	public Coffee() {
	}

	public Coffee(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Coffee(String name) {
		this(UUID.randomUUID().toString(), name);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}


/* Chapter 5 */
@ConfigurationProperties(prefix = "greeting")
class Greeting {
	private String name;
	private String coffee;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoffee() {
		return coffee;
	}

	public void setCoffee(String coffee) {
		this.coffee = coffee;
	}
}

@RestController
@RequestMapping("/greeting")
class GreetingController {
	private final Greeting greeting;

	public GreetingController(Greeting greeting) {
		this.greeting = greeting;
	}

	@GetMapping("/name")
	String getGreeting() {
		return greeting.getName();
	}

	@GetMapping("/coffee")
	String getNameAndCoffee() {
		return greeting.getCoffee();
	}
}

/* 為了解決使用 @Value 的限制，改使用@ConfigurationProperties，它提供可被工具驗證性
 * 步驟一：在 Model Greeting Class 掛上 @ConfigurationProperties(prefix = "greeting")
 * 步驟二：在app void main 進入點 掛上 @ConfigurationPropertiesScan
 * 步驟三：為了能讓 Spring 看得懂 annotation，需要 import dependency configuration-processor 到 pom.xml
 * 步驟四：寫一個 GreetingController 並且注入 Greeting 這個 bean，且 Mapping 進去後，return 使用 greeting.xxx
 * 步驟五：application.properties 使用 xxx.xxx 的方式
 * */