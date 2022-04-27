package com.example.testDemo;

import jdk.jfr.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

@SpringBootApplication
public class TestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestDemoApplication.class, args);
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

		// 步驟四：建立咖啡資料
		coffeeRepository.saveAll(List.of(
				new Coffee("Café Cereza"),
				new Coffee("Café Ganador"),
				new Coffee("Café Lareño"),
				new Coffee("Café Três Pontas")
		));
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
		// 不存在 => 新增(CREATED)，存在 => 更新(HttpStatus.OK)
		return (!coffeeRepository.existsById(id)) ?
				new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED) :
				new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK);
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