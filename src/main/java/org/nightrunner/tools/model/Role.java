package org.nightrunner.tools.model;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Created by NightRunner on 2016-02-24.
 */
public class Role {

    private static int indexCounter = 1;

    public Role(Item item, Organization sourceOrganization, Collection<Organization> useOrganizations,
                Collection<Organization> disableOrganizations, CentralizeType centralizeType) {

        Assert.notNull(item);
        Assert.notNull(sourceOrganization);
        Assert.notNull(useOrganizations);

        for (Organization useOrganization : useOrganizations) {
            Assert.notNull(useOrganization);
        }

        if (CollectionUtils.isNotEmpty(disableOrganizations)) {
            for (Organization organization : disableOrganizations) {
                Assert.notNull(organization);
            }
        }

        this.item = item;
        this.sourceOrganization = sourceOrganization;
        this.useOrganizations = useOrganizations;
        this.disableOrganizations = disableOrganizations;
        this.centralizeType = centralizeType;
        this.index = indexCounter++;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Organization getSourceOrganization() {
        return sourceOrganization;
    }

    public void setSourceOrganization(Organization sourceOrganization) {
        this.sourceOrganization = sourceOrganization;
    }

    public Collection<Organization> getUseOrganizations() {
        return useOrganizations;
    }

    public void setUseOrganizations(Collection<Organization> useOrganizations) {
        this.useOrganizations = useOrganizations;
    }

    public Collection<Organization> getDisableOrganizations() {
        return disableOrganizations;
    }

    public void setDisableOrganizations(Collection<Organization> disableOrganizations) {
        this.disableOrganizations = disableOrganizations;
    }

    public String getOutputFileName() {
        return index + "" + sourceOrganization.getName() + "_" + item.getName();
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public CentralizeType getCentralizeType() {
        return centralizeType;
    }

    public void setCentralizeType(CentralizeType centralizeType) {
        this.centralizeType = centralizeType;
    }

    private Item item;

    private Organization sourceOrganization;

    private Collection<Organization> useOrganizations;

    private Collection<Organization> disableOrganizations;

    private CentralizeType centralizeType;

    private String outputFileName;

    private int index = 0;

}
