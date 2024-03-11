package com.emi.onlineshop.services;

import com.emi.onlineshop.dtos.CartItemResponse;
import com.emi.onlineshop.models.CartItem;
import com.emi.onlineshop.models.Inventory;
import com.emi.onlineshop.models.Product;
import com.emi.onlineshop.models.User;
import com.emi.onlineshop.repositories.CartItemRepository;
import com.emi.onlineshop.repositories.InventoryRepository;
import com.emi.onlineshop.repositories.ProductRepository;
import com.emi.onlineshop.repositories.UserRepository;
import com.emi.onlineshop.security.SecurityUser;
import com.emi.onlineshop.utils.Mapper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CartItemService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final CartItemRepository cartItemRepository;
    private final Mapper mapper;

    public CartItemService(UserRepository userRepository, ProductRepository productRepository, InventoryRepository inventoryRepository, CartItemRepository cartItemRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.cartItemRepository = cartItemRepository;
        this.mapper = mapper;
    }

    public String addProductToCart(String productCode, short quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero!");
        }

        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = userRepository.findUserByEmail(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("No user found"));

        Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("No product found"));

        Inventory productInventory = inventoryRepository.findByCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("No product found in inventory"));

        if (productInventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock");
        }

        CartItem cartItem = cartItemRepository.findByUser_EmailAndProduct_Code(user.getEmail(), productCode)
                .orElseGet(CartItem::new);

        if (null == cartItem.getProduct()) {
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity((short) (cartItem.getQuantity() + quantity));
        }

        cartItemRepository.save(cartItem);

        return "Product " + productCode + " added successfully.";
    }

    @Transactional
    public String deleteProductFromCart(String productCode) {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        long removedProducts = cartItemRepository.deleteByUser_EmailAndProduct_Code(principal.getUsername(), productCode);

        return removedProducts > 0 ?
                "The product has been removed from the shopping cart." :
                "The product is not in the cart";
    }

    public List<CartItemResponse> getMappedCartForUser() {
        return getCartForUser()
                .stream()
                .map(this::mapCartItem)
                .toList();
    }

    public List<CartItem> getCartForUser() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cartItemRepository.findByUser_Email(principal.getUsername());
    }

    public String deleteCartForUser() {
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<CartItem> cartItems = cartItemRepository.findByUser_Email(principal.getUsername());

        if (cartItems.isEmpty()) {
            return "The cart is already empty";
        }
        cartItemRepository.deleteAll(cartItems);

        return "The cart is now empty";
    }

    private CartItemResponse mapCartItem(CartItem cartItem) {
        return new CartItemResponse(
                mapper.mapProductResponse(cartItem.getProduct()),
                cartItem.getQuantity(),
                mapper.mapUserResponse(cartItem.getUser()),
                cartItem.getSubtotal()
        );
    }

}
