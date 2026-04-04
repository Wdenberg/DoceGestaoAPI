package com.wdenberg.docegestao.sale.service;



import com.wdenberg.docegestao.client.repository.ClientRepository;
import com.wdenberg.docegestao.common.exception.ResouceNotFoundExceptio;
import com.wdenberg.docegestao.recipe.repository.RecipeRepository;
import com.wdenberg.docegestao.sale.dto.SaleItemRequest;
import com.wdenberg.docegestao.sale.dto.SaleRequest;
import com.wdenberg.docegestao.sale.dto.SaleResponse;
import com.wdenberg.docegestao.sale.entity.Sale;
import com.wdenberg.docegestao.sale.entity.SaleItem;
import com.wdenberg.docegestao.sale.entity.SaleStatus;
import com.wdenberg.docegestao.sale.mapper.SaleMapper;
import com.wdenberg.docegestao.sale.repository.SaleRepository;
import com.wdenberg.docegestao.sale.repository.SaleSpecification;
import com.wdenberg.docegestao.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final RecipeRepository recipeRepository;
    private final SaleMapper saleMapper;


    @Transactional
    public SaleResponse create(UUID userId, SaleRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Usuário não encontrado"));

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setSaleDate(request.saleDate());
        sale.setDiscount(request.discount() == null ? BigDecimal.ZERO : request.discount());
        sale.setPaymentMethod(request.paymentMethod());
        sale.setNotes(request.notes());
        sale.setStatus(SaleStatus.PENDING);

        if (request.clientId() != null) {
            var client = clientRepository.findByIdAndUser_Id(request.clientId(), userId)
                    .orElseThrow(() -> new ResouceNotFoundExceptio("Cliente não encontrado"));
            sale.setClient(client);
        }

        recalculate(sale);
        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Transactional
    public SaleResponse addItem(UUID saleId, UUID userId, SaleItemRequest request) {
        Sale sale = saleRepository.findByIdAndUser_Id(saleId, userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Venda não encontrada"));

        var recipe = recipeRepository.findByIdAndUser_Id(request.recipeId(), userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Receita não encontrada"));

        SaleItem item = new SaleItem();
        item.setSale(sale);
        item.setRecipe(recipe);
        item.setQuantity(request.quantity());
        item.setUnitPrice(request.unitPrice());
        item.setTotalPrice(request.quantity().multiply(request.unitPrice()));

        sale.getItems().add(item);
        recalculate(sale);

        if (sale.getClient() != null) {
            var client = sale.getClient();
            client.setTotalOrders(client.getTotalOrders() + 1);
            client.setTotalSpent(client.getTotalSpent().add(item.getTotalPrice()));
            client.setLastOrderAt(OffsetDateTime.now());
        }

        return saleMapper.toResponse(saleRepository.save(sale));
    }

    @Transactional(readOnly = true)
    public Page<SaleResponse> findAll(UUID userId, SaleStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return saleRepository.findAll(SaleSpecification.byFilters(userId, status, startDate, endDate), pageable)
                .map(saleMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public SaleResponse findById(UUID id, UUID userId) {
        return saleRepository.findByIdAndUser_Id(id, userId)
                .map(saleMapper::toResponse)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Venda não encontrada"));
    }

    @Transactional
    public SaleResponse updateStatus(UUID id, UUID userId, SaleStatus status) {
        Sale sale = saleRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Venda não encontrada"));
        sale.setStatus(status);
        return saleMapper.toResponse(saleRepository.save(sale));
    }

    private void recalculate(Sale sale) {
        BigDecimal subtotal = sale.getItems().stream()
                .map(SaleItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        sale.setSubTotal(subtotal);
        sale.setTotalAmount(subtotal.subtract(sale.getDiscount() == null ? BigDecimal.ZERO : sale.getDiscount()));
    }
}
