
package com.csipon.crm.domain.real;

import com.csipon.crm.domain.model.Product;
import com.csipon.crm.domain.model.ProductParam;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "product_param")
public class RealProductParam implements ProductParam{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "param_name")
    private String paramName;
    @Column
    private String value;
    @JoinColumn(name = "product_id", table = "product", referencedColumnName = "id")
    private Product product;

    public RealProductParam() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getParamName() {
        return paramName;
    }

    @Override
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Product getProduct() {
        return product;
    }
    
    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.paramName);
        hash = 61 * hash + Objects.hashCode(this.value);
        hash = 61 * hash + Objects.hashCode(this.product);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RealProductParam other = (RealProductParam) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.paramName, other.paramName)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.product, other.product)) {
            return false;
        }
        return true;
    }    
}
