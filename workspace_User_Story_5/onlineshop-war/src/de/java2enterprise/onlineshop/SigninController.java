package de.java2enterprise.onlineshop;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.java2enterprise.onlineshop.model.Customer;


@Named
@SessionScoped
public class SigninController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private Customer customer;
	
	private String email;
	
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public String find() {
		try {
			TypedQuery<Customer> query = em.createQuery(
				"SELECT c FROM Customer c "
				+ "WHERE c.email= :email "
				+ "AND c.password= :password ",
				Customer.class);
			query.setParameter("email", email);
			query.setParameter("password", password);
			List<Customer> list = query.getResultList();
			if(list != null && list.size() > 0) {
				customer = list.get(0);
				Logger.getLogger(SigninController.class.getCanonicalName())
				.log(Level.INFO, "Speicherung: " + customer);
				FacesMessage m = new FacesMessage(
						"Succesfully signed in under id " +
						customer.getId());
				FacesContext
					.getCurrentInstance()
					.addMessage("signinForm", m);
			}
		} catch (Exception e) {
			Logger.getLogger(SigninController.class.getCanonicalName())
				.log(Level.WARNING, "Fehler: " + e.getMessage());
			FacesMessage m = new FacesMessage(
					FacesMessage.SEVERITY_WARN,
					e.getMessage(),
					e.getCause().getMessage());
			FacesContext
				.getCurrentInstance()
				.addMessage("signinForm", m);
		}
		return "signin";
	}
	
}
