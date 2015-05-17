package nl.b3p.kaartenbalie.struts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nl.b3p.ogc.ServiceProviderInterface;

/**
 *
 * @author Chris
 */
public class ServiceProviderDTO implements ServiceProviderInterface, Comparable {

    private Integer id;
    private String abbr;
    private String name;
    private String title;
    private String givenName;
    private String url;
    private Date updatedDate;
    private String type;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the abbr
     */
    public String getAbbr() {
        return abbr;
    }

    /**
     * @param abbr the abbr to set
     */
    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the givenName
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * @param givenName the givenName to set
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the updatedDate
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    static public ServiceProviderDTO createInstance(ServiceProviderInterface spi) {
        ServiceProviderDTO dto = new ServiceProviderDTO();
        dto.setId(spi.getId());
        dto.setAbbr(spi.getAbbr());
        dto.setGivenName(spi.getGivenName());
        dto.setName(spi.getName());
        dto.setTitle(spi.getTitle());
        dto.setType(spi.getType());
        dto.setUpdatedDate(spi.getUpdatedDate());
        dto.setUrl(spi.getUrl());
        return dto;
    }

    static public List<ServiceProviderDTO> createList(List<ServiceProviderInterface> spil) {
        if (spil == null || spil.isEmpty()) {
            return null;
        }

        List dtoList = new ArrayList();
        for (ServiceProviderInterface spi : spil) {
            ServiceProviderDTO dto = createInstance(spi);
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServiceProviderDTO other = (ServiceProviderDTO) obj;
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        if ((this.abbr == null) ? (other.abbr != null) : !this.abbr.equals(other.abbr)) {
            return false;
        }
        return true;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return 1;
        }
        if (getClass() != obj.getClass()) {
            return 1;
        }
        final ServiceProviderDTO other = (ServiceProviderDTO) obj;

        if (this.type == null && other.type == null) {
            if (this.abbr == null && other.abbr == null) {
                return 0;
            }
            if (this.abbr == null) {
                return -1;
            }
            if (other.abbr == null) {
                return 1;
            }
            return this.abbr.compareTo(other.abbr);
        }
        if (this.type == null) {
            return -1;
        }
        if (other.type == null) {
            return 1;
        }
        return this.type.compareTo(other.type);
    }
}
