package com.banking.operaciones.model;


import com.banking.core.infraestrutura.model.BaseEntidadAuditoria;
import com.banking.operaciones.model.enums.TipoCuenta;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "TAB_CUENTA", schema = "operaciones")
public class BanCuenta extends BaseEntidadAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldoInicial;
    private  boolean estado;
    private BigDecimal saldoDisponible;
    private String identificacionCliente;
    private Long clienteId;


    @Id
    @GeneratedValue(generator = "secTabCuenta", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "secTabCuenta", allocationSize = 1, initialValue = 1, sequenceName = "SEC_CUENTA")
    @Column(name = "ID_CUENTA")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "numero_cuenta")
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_CUENTA", nullable = false, length = 3)
    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    @Column(name = "SALDO_INICIAL")
    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }


    @Column(name = "ESTADO")
    public boolean getEstado() {
        return estado;
    }


    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Column(name = "IDENTIFICACION_CLIENTE", length = 13)
    public String getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    @Column(name = "saldo_disponible" )
    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    @Column(name = "CLIENTE_ID", nullable = false)
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
