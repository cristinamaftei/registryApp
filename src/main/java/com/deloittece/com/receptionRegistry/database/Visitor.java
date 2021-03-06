package com.deloittece.com.receptionRegistry.database;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "visitors", uniqueConstraints = { @UniqueConstraint(columnNames = { "IC_INFO" }) })

public class Visitor implements Persistable<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VISITOR_ID")
	private Long id;

	@NotNull(message = "Please enter you name")
	@Column(name = "FULL_NAME")
	@Size(min = 2, max = 40, message = "Name must be between 2 and 40 characters")
	private String fullName;

	@NotNull(message = "Please enter the number of your identity card or passport")
	@Column(name = "IC_INFO", nullable = false)
	@Pattern(regexp = "^([A-Z | a-z]{2})?([0-9]{6,9})$", message = "Please enter the correct number of your identity card or passport")
	private String identityCardInfo;

	@Column(name = "EMAIL")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "Please enter a valid email address")
	private String email;

	@Lob()
	@Column(name = "SIGNATURE")
	private byte[] signature;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, mappedBy = "visitor")
	private List<Visit> visits = new ArrayList<>();

	public Visitor() {
		// super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getIdentityCardInfo() {
		return identityCardInfo;
	}

	public void setIdentityCardInfo(String identityCardInfo) {
		this.identityCardInfo = identityCardInfo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

	public Visitor(Long id, String fullName, String email, String identityCardInfo, byte[] signature,
			List<Visit> visits) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.identityCardInfo = identityCardInfo;
		this.signature = signature;
		this.visits = visits;
	}

	public Visitor(Long id, String fullName, String email, String identityCardInfo) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.identityCardInfo = identityCardInfo;
	}

	public Visitor(String fullName, String identityCardInfo) {
		super();
		this.fullName = fullName;
		this.identityCardInfo = identityCardInfo;
	}

	// a visitor will be register in the database only once
	@Override
	public boolean isNew() {
		return this.id == null;
	}

}
