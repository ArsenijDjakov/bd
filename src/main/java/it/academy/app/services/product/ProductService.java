package it.academy.app.services.product;

import it.academy.app.exception.IncorrectDataException;
import it.academy.app.models.product.Product;
import it.academy.app.repositories.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll().stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
    }

    public Product getProductById(long productId) throws IncorrectDataException {
        Product product = productRepository.findById(productId);
        if (product == null) {
            throw new IncorrectDataException("Product does not found!");
        }
        return product;
    }

    public List<Product> getProductsByCategory(long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
    }

    public List<Product> getProductsBySubCategory(long subCategoryId) {
        return productRepository.findBySubCategoryId(subCategoryId).stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
    }

    public List<Product> searchByName(String search) {
        search = search.replaceAll("[aAąĄcCčČeEęĘėĖiIįĮsSšŠuUųŲūŪzZžŽ]", "_");
        return productRepository.findByNameLike(search);
    }

}
