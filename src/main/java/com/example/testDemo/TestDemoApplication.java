package com.example.testDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

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
	private List<Coffee> coffees = new ArrayList<>();

	public RestApiDemoController() {
		coffees.addAll(
				List.of(
						new Coffee("Caf'e Cereza"),
						new Coffee("Caf'e Ganador"),
						new Coffee("Caf'e Lareno"),
						new Coffee("Caf'e Tres Pontas")
				)
		);
		coffees.add(new Coffee("Caf'e Brown"));
	}

	// 得到全部的咖啡資料
	@RequestMapping(method = RequestMethod.GET)
//	@GetMapping("/coffees")
	Iterable<Coffee> getCoffees() {
		return coffees;
	}

	// 得到某一筆的咖啡資料，傳入 id 查詢
	@GetMapping("/{id}")
	Optional<Coffee> getCoffeeById(@PathVariable String id) {
		for (Coffee c: coffees) {
			if (c.getId().equals(id)) {
				return Optional.of(c);
			}
		}
		return Optional.empty();
	}

//	@GetMapping("/coffees/{id}")
//	List<Coffee> getCoffeeById(@PathVariable String id) {
//		for (Coffee c: coffees) {
//			if (c.getId().equals(id)) {
//				return List.of(c);
//			}
//		}
//		return Collections.emptyList();
//	}


	@PostMapping()
	List<Coffee> addCoffee(@RequestBody Coffee coffee) {
		coffees.add(new Coffee(coffee.getName()));
		return coffees;
	}
	@PostMapping("/coffees2")
	Coffee postCoffee(@RequestBody Coffee coffee) {
//		coffees.add(coffee); => 造成 id 為 null
		coffees.add(new Coffee(coffee.getName()));
		return coffee;
	}

	// 若無此更新資料就新增一筆，若有此資料就更新
	@PutMapping("/{id}")
	Coffee putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
		int coffeeIndex = -1;

		for (Coffee c: coffees) {
			if (c.getId().equals(id)) {
				coffeeIndex = coffees.indexOf(c);
				coffees.set(coffeeIndex, coffee);
			}
		}

		return (coffeeIndex == -1) ? postCoffee(coffee) : coffee;
	}

	@DeleteMapping("/{id}")
	void deleteCoffee(@PathVariable String id) {
		coffees.removeIf(c -> c.getId().equals(id));
	}
}


class Coffee {
	// 設為 final 只能被指派一次，且不能被修改
	private final String id;
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}