package sekoya.back.business.user.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import org.bindgen.Bindable;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.ValueBridgeRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.iglooproject.jpa.business.generic.model.GenericEntity;
import org.iglooproject.jpa.search.bridge.GenericEntityIdBridge;
import sekoya.back.business.organisation.model.Organisation;

@Indexed
@Bindable
@Cacheable
@Entity
public class UserOrganisation extends GenericEntity<Long, UserOrganisation> {

  private static final long serialVersionUID = 1L;

  public static final String ORGANISATION = "organisation";

  @Id private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private User user;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @GenericField(
      name = ORGANISATION,
      valueBridge = @ValueBridgeRef(type = GenericEntityIdBridge.class))
  private Organisation organisation;

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Organisation getOrganisation() {
    return organisation;
  }

  public void setOrganisation(Organisation organisation) {
    this.organisation = organisation;
  }
}
