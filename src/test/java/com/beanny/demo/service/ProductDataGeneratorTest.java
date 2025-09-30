package com.beanny.demo.service;

import com.beanny.demo.entity.Product;
import com.beanny.demo.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
@Transactional
class ProductDataGeneratorTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Rollback(false) // Set to true if you want to rollback after test
    void generateAndInsert100Products() {
        List<Product> products = generateFakeProducts(100);
        productRepository.saveAll(products);

        System.out.println("Successfully inserted " + products.size() + " fake products for testing!");
    }

    private List<Product> generateFakeProducts(int count) {
        List<Product> products = new ArrayList<>();
        Random random = new Random();

        String[] productNames = {
            "Laptop", "Mouse", "Keyboard", "Monitor", "Headphones", "Speaker", "Tablet",
            "Smartphone", "Camera", "Printer", "Router", "SSD", "RAM", "Motherboard",
            "Graphics Card", "Power Supply", "Case", "CPU", "Webcam", "Microphone",
            "External HDD", "USB Drive", "HDMI Cable", "Ethernet Cable", "Charger",
            "Dock Station", "Smartwatch", "Earbuds", "Gaming Chair", "Desk Lamp"
        };

        String[] brands = {
            "Apple", "Samsung", "Dell", "HP", "Lenovo", "ASUS", "Acer", "Sony",
            "LG", "Microsoft", "Logitech", "Razer", "Corsair", "Kingston", "WD"
        };

        String[] descriptions = {
            "High-quality product with excellent performance",
            "Perfect for professional use and gaming",
            "Compact design with powerful features",
            "Energy-efficient and eco-friendly",
            "Premium build quality with warranty",
            "Ideal for home and office use",
            "Latest technology with advanced features",
            "Durable and reliable performance",
            "Sleek design with modern aesthetics",
            "Best value for money product"
        };

        for (int i = 1; i <= count; i++) {
            Product product = new Product();

            // Generate unique product name by combining brand + product + number
            String brand = brands[random.nextInt(brands.length)];
            String productType = productNames[random.nextInt(productNames.length)];
            product.setProductName(brand + " " + productType + " " + (1000 + i));

            // Generate random price between $10 and $2000
            double price = 10.0 + (random.nextDouble() * 1990.0);
            product.setPrice(Math.round(price * 100.0) / 100.0); // Round to 2 decimal places

            // Random description
            product.setDescription(descriptions[random.nextInt(descriptions.length)]);

            products.add(product);
        }

        return products;
    }
}