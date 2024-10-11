package com.example.controller;

import com.example.model.entity.Category;
import com.example.model.entity.Product;
import com.example.model.service.category.CategoryService;
import com.example.model.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public String index(Model model){
        List<Product> products = productService.findAll();
        model.addAttribute("products",products);
        return "product/index";
    }
    @GetMapping("/add")
    public String add(Model model){
        Product product = new Product();
        model.addAttribute("product",product);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);
        return "product/add";
    }
    @PostMapping("/add")
    public String create(@ModelAttribute Product product, @RequestParam("imgFile") MultipartFile file){
        // xuw ly up anh
        String fileName = file.getOriginalFilename();
        String path = "D:\\Luannv\\FK_PT_2406\\MD3\\session09\\src\\main\\webapp\\uploads";
        File destination = new File(path+"/"+fileName);
        try {
            Files.write(destination.toPath(),file.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        product.setImage(fileName);
        if(productService.create(product)){
            return "redirect:/product";
        }
        return "product/add";
    }
}
