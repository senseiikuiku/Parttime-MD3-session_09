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
        String path = "E:\\academy\\MD3\\sesssion09\\src\\main\\webapp\\uploads";
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

    @GetMapping("/back")
    public String back(Model model){
        return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        Product product = productService.findById(id);
        model.addAttribute("product",product);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories",categories);
        return "product/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable int id, @ModelAttribute Product product, @RequestParam("imgFile") MultipartFile file, @RequestParam("currentImage") String currentImage){
        if(file.isEmpty()){
//            Không chọn ảnh mới hiển thị lại ảnh cũ
            product.setImage(currentImage);
        }else {
            // Xử lý nếu có ảnh mới
            String fileName = file.getOriginalFilename();
            String path = "E:\\academy\\MD3\\sesssion09\\src\\main\\webapp\\uploads";
            File destination = new File(path + "/" + fileName);
            try {
                Files.write(destination.toPath(), file.getBytes(), StandardOpenOption.CREATE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            product.setImage(fileName);
        }
        if (productService.update(product)){
            return "redirect:/product";
        }
        return "redirect:/product/edit/{id}";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id){
        productService.delete(id);
        return "redirect:/product";
    }
}
