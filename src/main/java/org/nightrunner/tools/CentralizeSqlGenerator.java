package org.nightrunner.tools;

import org.apache.commons.collections.CollectionUtils;
import org.nightrunner.tools.model.CentralizeType;
import org.nightrunner.tools.model.Item;
import org.nightrunner.tools.model.Organization;
import org.nightrunner.tools.model.Role;
import org.nightrunner.tools.util.BaseEntityUtil;
import org.nightrunner.tools.util.ValueGetter;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;

/**
 * Created by NightRunner on 2016-02-24.
 */
public class CentralizeSqlGenerator {


    private static Collection<Organization> haveBudgetOrganizations;
    private static Collection<Organization> notHaveBudgetOrganizations;
    private static Collection<Organization> allOrganizations;

    private static Map<String, Organization> organizationMap;

    private static Map<String, Item> itemMap;

    private static List<String> getCenterDeleteAndInsertSQL(Long itemId, Collection<Organization> organizations,
                                                            CentralizeType centralizeType) {

        List<String> centerDeleteAndInsertSQLs = new ArrayList<String>();

        for (Organization organization : organizations) {

            StringBuffer deleteBuffer = new StringBuffer();

            deleteBuffer.append("delete BMS_IC_CENTER where item_id=" + itemId + " and books_Id=1 and "
                    + "domain_Id='Default' and organization_Id=" + organization.getId() + " and period='"
                    + PERIOD + "' and data_type='Current';");

            centerDeleteAndInsertSQLs.add(deleteBuffer.toString());

            StringBuffer insertBuffer = new StringBuffer();
            insertBuffer.append("insert into BMS_IC_CENTER ");
            insertBuffer.append("(BOOKS_ID, DATA_TYPE, ITEM_ID, ORGANIZATION_ID, PERIOD, CREATE_TIME, "
                    + "CREATOR_ID, DELETABLE, DELETED, ENABLED, INHERENT, LAST_MODIFIED_TIME, LAST_MODIFIER_ID,"
                    + " DOMAIN_ID,NOTE, SOURCE, VERSION_ID, IS_CENTRALIZED) VALUES ");

            insertBuffer.append("(1,'Current'," + itemId + "," + organization.getId() + "," + PERIOD + ",null,"
                    + OPERATOR_ID + ",1,0,1,0,null,9789,'Default'"
                    + ",null,null,null," + centralizeType.getId() + ");");
            centerDeleteAndInsertSQLs.add(insertBuffer.toString());
        }
        return centerDeleteAndInsertSQLs;
    }

    private static final long PERIOD = 201500;
    private static final long OPERATOR_ID = 9789;
    private static final String DOMAIN_ID = "Default";

    private static final String OUTPUT_PATH = "E:\\Projects\\人民医院项目\\脚本\\归口关系生成\\";

    public static File getOutputFile(String name) {
        return new File(OUTPUT_PATH + name + ".sql");
    }

    public static void writeToOutputFile(String name, Collection<String> rows) throws Exception {

        PrintWriter writer = new PrintWriter(getOutputFile(name), "utf-8");

        for (String row : rows) {
            writer.println(row);
        }

        writer.close();
    }

    private static List<String> getCenterDetailDeleteAndInsertSQL(Long itemId, Long sourceOrgId,
                                                                  Collection<Organization> useOrganizations) {
        List<String> deleteAndInsertCenterDetailSQLs = new ArrayList<String>();
        for (Organization useOrganization : useOrganizations) {

            StringBuffer deleteBuffer = new StringBuffer();

            deleteBuffer.append("delete BMS_IC_CENTER_DTL where itemid=" + itemId + " and booksId=1 and "
                    + "domain_Id='Default' and use_organization_Id=" + useOrganization.getId() + " and  "
                    + "source_organization_Id=" + sourceOrgId + " and period='" + PERIOD + "';");

            deleteAndInsertCenterDetailSQLs.add(deleteBuffer.toString());

            StringBuffer buffer = new StringBuffer();
            buffer.append("insert INTO  BMS_IC_CENTER_DTL (BOOKSID, ITEMID, PERIOD, SOURCE_ORGANIZATION_ID,"
                    + " USE_ORGANIZATION_ID, CREATE_TIME, CREATOR_ID, DELETABLE, DELETED, ENABLED, INHERENT, "
                    + "LAST_MODIFIED_TIME, LAST_MODIFIER_ID, DOMAIN_ID) values");
            buffer.append("(1," + itemId + ",'" + PERIOD + "'," + sourceOrgId + "," + useOrganization.getId() + ",null,"
                    + OPERATOR_ID
                    + ",1,0,1,0,null," + OPERATOR_ID + ",'" + DOMAIN_ID + "');");

            deleteAndInsertCenterDetailSQLs.add(buffer.toString());
        }

        return deleteAndInsertCenterDetailSQLs;
    }

    private static Organization getOrganizationByName(String organizationName) {
        Organization organization = organizationMap.get(organizationName);
        Assert.notNull(organization, "名为:[" + organizationName + "]的组织不存在.");
        return organization;
    }

    private static Item getItemByName(String itemName) {
        Item item = itemMap.get(itemName);
        Assert.notNull(item, "名为:[" + itemName + "]的指标不存在.");
        return item;
    }

    private static void getInitRoles(List<Role> roles) {
        Collection<Organization> uses;
        roles.add(new Role(getItemByName("信息网络购建"), getOrganizationByName("医学信息中心"),
                Arrays.asList(getOrganizationByName("设备处")),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("医学信息中心"))),
                CentralizeType.MANY_BROTHER));

        roles.add(new Role(getItemByName("网络信息运行维护费"), getOrganizationByName("宣传处"),
                Arrays.asList(getOrganizationByName("宣传处")),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"),
                        getOrganizationByName("宣传处"), getOrganizationByName("医学信息中心"))), CentralizeType.SELF));

        roles.add(new Role(getItemByName("网络信息运行维护费"), getOrganizationByName("保卫处"),
                Arrays.asList(getOrganizationByName("保卫处")),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"),
                        getOrganizationByName("宣传处"), getOrganizationByName("医学信息中心"))), CentralizeType.SELF));

        roles.add(new Role(getItemByName("网络信息运行维护费"), getOrganizationByName("医学信息中心"),
                Arrays.asList(getOrganizationByName("设备处")),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"),
                        getOrganizationByName("宣传处"), getOrganizationByName("医学信息中心"))),
                CentralizeType.MANY_BROTHER));

        roles.add(new Role(getItemByName("专业设备购置"), getOrganizationByName("设备处"),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"))),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"),
                        getOrganizationByName("设备处"))), CentralizeType.MANY_BROTHER));

        roles.add(new Role(getItemByName("专业设备购置"), getOrganizationByName("保卫处"),
                Arrays.asList(getOrganizationByName("保卫处")),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"),
                        getOrganizationByName("设备处"))), CentralizeType.SELF));

        roles.add(new Role(getItemByName("专业设备维修"), getOrganizationByName("设备处"),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"))),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"),
                        getOrganizationByName("设备处"))), CentralizeType.MANY_BROTHER));

        roles.add(new Role(getItemByName("专业设备维修"), getOrganizationByName("保卫处"),
                Arrays.asList(getOrganizationByName("保卫处")),
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("保卫处"),
                        getOrganizationByName("设备处"))), CentralizeType.SELF));

        for (Organization organization : haveBudgetOrganizations) {
            roles.add(new Role(getItemByName("本市差旅费"), organization, Arrays.asList(organization), null,
                    CentralizeType.SELF));
            roles.add(new Role(getItemByName("外埠差旅费"), organization, Arrays.asList(organization), null,
                    CentralizeType.SELF));
        }

        uses = new ArrayList<Organization>(notHaveBudgetOrganizations);
        uses.add(getOrganizationByName("科研处"));
        roles.add(new Role(getItemByName("本市差旅费"), getOrganizationByName("科研处"), uses, null,
                CentralizeType.MANY_BROTHER));

        roles.add(new Role(getItemByName("外埠差旅费"), getOrganizationByName("科研处"), uses, null,
                CentralizeType.MANY_BROTHER));

        for (Organization organization : haveBudgetOrganizations) {
            roles.add(new Role(getItemByName("奖金"), organization, Arrays.asList(organization), null,
                    CentralizeType.SELF));
        }

        uses = new ArrayList<Organization>(notHaveBudgetOrganizations);
        uses.add(getOrganizationByName("运营管理处"));
        roles.add(new Role(getItemByName("奖金"), getOrganizationByName("运营管理处"), uses, null,
                CentralizeType.MANY_BROTHER));

        //版面费  科研处  全院
        roles.add(new Role(getItemByName("版面费"), getOrganizationByName("科研处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("科研处"))),
                CentralizeType.MANY_BROTHER));

        //高值耗材  设备处  全院

        roles.add(new Role(getItemByName("高值耗材"), getOrganizationByName("设备处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("设备处"))),
                CentralizeType.MANY_BROTHER));

        //基本工资  人事处  全院
        roles.add(new Role(getItemByName("基本工资"), getOrganizationByName("人事处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("人事处"))),
                CentralizeType.MANY_BROTHER));

        //绩效工资  人事处  全院
        roles.add(new Role(getItemByName("绩效工资"), getOrganizationByName("人事处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("人事处"))),
                CentralizeType.MANY_BROTHER));

        //津贴补贴  人事处  全院
        roles.add(new Role(getItemByName("津贴补贴"), getOrganizationByName("人事处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("人事处"))),
                CentralizeType.MANY_BROTHER));

        //缴纳税费  财务处  全院
        roles.add(new Role(getItemByName("缴纳税费"), getOrganizationByName("财务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("财务处"))),
                CentralizeType.MANY_BROTHER));

        //营业税  财务处  全院
        roles.add(new Role(getItemByName("营业税"), getOrganizationByName("财务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("财务处"))),
                CentralizeType.MANY_BROTHER));

        //城市维护建设税  财务处  全院
        roles.add(new Role(getItemByName("城市维护建设税"), getOrganizationByName("财务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("财务处"))),
                CentralizeType.MANY_BROTHER));

        //房产税  财务处  全院
        roles.add(new Role(getItemByName("房产税"), getOrganizationByName("财务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("财务处"))),
                CentralizeType.MANY_BROTHER));

        //城镇土地使用税  财务处  全院
        roles.add(new Role(getItemByName("城镇土地使用税"), getOrganizationByName("财务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("财务处"))),
                CentralizeType.MANY_BROTHER));
    }

    private static Collection<Organization> getExceptedOrgs(Collection<Organization> organizations,
                                                            Collection<Organization> exceptOrganizations) {

        List<Organization> exceptedOrgs = new ArrayList<Organization>(organizations);
        BIG:
        for (int i = 0; i < exceptedOrgs.size(); i++) {
            Organization tempOrganization = exceptedOrgs.get(i);
            for (Organization tempExcept : exceptOrganizations) {
                if (tempOrganization.getId().equals(tempExcept.getId())) {
                    exceptedOrgs.remove(i);
                    i--;
                    continue BIG;
                }
            }
        }


        return exceptedOrgs;
    }

    private static void initItem() throws IOException {
        Collection<Item> items = new ArrayList<Item>();

        String itemFileName = "E:\\Projects\\人民医院项目\\item.txt";
        File organizationFile = new File(itemFileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(organizationFile), "UTF-8"));

        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] lineColumns = line.split(",");

            Item item = new Item();
            item.setId(Long.parseLong(lineColumns[0]));
            item.setName(lineColumns[1]);

            items.add(item);
        }

        itemMap = BaseEntityUtil.getEntityMap(items, new ValueGetter<Item, String>() {
            public String getValue(Item object) {
                return object.getName();
            }
        });
    }

    public static void main(String[] args) throws Exception {

        //读取组织信息
        initOrganization();

        //指标信息
        initItem();

        List<Role> roles = new ArrayList<Role>();

        //getInitRoles(roles);

        roles.add(new Role(getItemByName("医用材料费"), getOrganizationByName("总务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("总务处"))),
                CentralizeType.MANY_BROTHER));

        roles.add(new Role(getItemByName("办公材料费"), getOrganizationByName("总务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("总务处"))),
                CentralizeType.MANY_BROTHER));

        roles.add(new Role(getItemByName("印刷材料费"), getOrganizationByName("总务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("总务处"))),
                CentralizeType.MANY_BROTHER));

        roles.add(new Role(getItemByName("其他材料费"), getOrganizationByName("总务处"), allOrganizations,
                getExceptedOrgs(allOrganizations, Arrays.asList(getOrganizationByName("总务处"))),
                CentralizeType.MANY_BROTHER));

        Collection<String> sqls = new ArrayList<String>();
        for (Role role : roles) {


            //生成sql
            Item item = role.getItem();

            Organization sourceOrganization = role.getSourceOrganization();
            Collection<Organization> useOrganizations = role.getUseOrganizations();
            Collection<Organization> disabledOrganizations = role.getDisableOrganizations();

            String outFileName = role.getOutputFileName();

            //归口表SQL
            sqls.add("--归口表删除并添加新数据");
            sqls.addAll(getCenterDeleteAndInsertSQL(item.getId(), Arrays.asList(sourceOrganization),
                    CentralizeType.MANY_BROTHER));
            if (CollectionUtils.isNotEmpty(disabledOrganizations)) {
                sqls.addAll(getCenterDeleteAndInsertSQL(item.getId(), disabledOrganizations, CentralizeType.DISABLED));
            }


            sqls.add("--归口明细表删除并添加新数据");
            sqls.addAll(getCenterDetailDeleteAndInsertSQL(item.getId(), sourceOrganization.getId(), useOrganizations));

            //归口Detail表SQL
            //writeToOutputFile(outFileName, sqls);
        }

        //sqls
        writeToOutputFile("update", sqls);


        System.out.print("over");
    }

    private static void initOrganization() throws IOException {

        haveBudgetOrganizations = new ArrayList<Organization>();
        notHaveBudgetOrganizations = new ArrayList<Organization>();
        allOrganizations = new ArrayList<Organization>();

        final String organizationFileName = "E:\\Projects\\人民医院项目\\organization.txt";
        File organizationFile = new File(organizationFileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(organizationFile), "UTF-8"));

        String line = null;
        while ((line = reader.readLine()) != null) {
            String[] lineColumns = line.split(",");

            Organization organization = new Organization();
            organization.setId(Long.parseLong(lineColumns[0]));
            organization.setCode(lineColumns[1]);
            organization.setName(lineColumns[2]);

            if (lineColumns.length == 4) {
                notHaveBudgetOrganizations.add(organization);
            } else if (lineColumns.length == 5) {
                haveBudgetOrganizations.add(organization);
            }
        }

        allOrganizations.addAll(haveBudgetOrganizations);
        allOrganizations.addAll(notHaveBudgetOrganizations);


        organizationMap = BaseEntityUtil.getEntityMap(allOrganizations, new ValueGetter<Organization, String>() {
            public String getValue(Organization object) {
                return object.getName();
            }
        });
    }
}

