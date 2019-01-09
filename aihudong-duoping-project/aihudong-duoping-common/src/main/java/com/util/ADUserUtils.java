package com.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

/**
 * @Description:对AD域用户的增删改查操作
 * @author moonxy
 * @date 2018-05-15
 */
public class ADUserUtils {
    DirContext dc = null;
    public String root = "OU=学生,OU=学院,OU=NEC,OU=Person,OU=vmware,DC=hdycjy,DC=com"; // LDAP的根节点的DC  
    
    /**
     * @Description:程序入口
     * @author moonxy
     * @throws NamingException 
     * @date 2018-05-15
     */
    public static void main(String[] args) throws NamingException {
        ADUserUtils utils = new ADUserUtils();
        
//        utils.add("JimGreen");
        
        SearchResult sr = utils.searchByUserName(utils.root, "huihui");
//        System.out.println(sr.getName());
        System.out.println(sr.getAttributes().get("givenName").get(0));
//        
//        utils.modifyInformation(sr.getName(), "M1380005");
        
//        utils.searchInformation(utils.root);
        
//        utils.renameEntry("CN=JimGreen,OU=Java开发组,OU=软件研发部,DC=moonxy,DC=com", "CN=JimGreen,OU=Web前端组,OU=软件研发部,DC=moonxy,DC=com");
        
//        utils.delete("CN=JimGreen,OU=Web前端组,OU=软件研发部,DC=moonxy,DC=com");
        
        utils.close();
    }
    
    /**
     * 初始化
     */
    public ADUserUtils() {
        super();
        init();
    }
    
    public String testConnect() {
    	Properties env = new Properties();
        /*String adminName = "ybjt\\administrator";//username@domain
        String adminPassword = "1qaz@WSX";//password
        String ldapURL = "LDAP://192.168.10.222:389";//ip:port
*/      String adminName = "CN=Administrator,CN=Users,DC=hdycjy,DC=com";//username@domain
        String adminPassword = "huagongYCJY@789.com";//password
        String ldapURL = "LDAP://172.25.1.17:389";//ip:port
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别："none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, adminName);
        env.put(Context.SECURITY_CREDENTIALS, adminPassword);
        env.put(Context.PROVIDER_URL, ldapURL);
        //env.put("java.naming.ldap.factory.socket", "org.utils.ad.DummySSLSocketFactory");
       /* env.put(Context.SECURITY_PROTOCOL, "ssl");
        String keystore = "C:\\Program Files\\Java\\jdk1.8.0_191\\jre\\lib\\security\\cacerts"; 
    	System.setProperty("javax.net.ssl.trustStore", keystore);
    	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");*/
    	 try {
             dc = new InitialLdapContext(env, null);
             return "AD域服务连接认证成功";
         } catch (Exception e) {
             System.out.println("AD域服务连接认证失败");
             e.printStackTrace();
             return  e.getMessage();
         }
    	
    }

    /**
     * @Description:初始化AD域服务连接
     * @author moonxy
     * @date 2018-05-15
     */
    public void init() {
    	
        Properties env = new Properties();
        /*String adminName = "ybjt\\administrator";//username@domain
        String adminPassword = "1qaz@WSX";//password
        String ldapURL = "LDAP://192.168.10.222:389";//ip:port
*/      String adminName = "CN=Administrator,CN=Users,DC=hdycjy,DC=com";//username@domain
        String adminPassword = "huagongYCJY@789.com";//password
        String ldapURL = "LDAP://172.25.1.17:389";//ip:port
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//LDAP访问安全级别："none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, adminName);
        env.put(Context.SECURITY_CREDENTIALS, adminPassword);
        env.put(Context.PROVIDER_URL, ldapURL);
        //env.put("java.naming.ldap.factory.socket", "org.utils.ad.DummySSLSocketFactory");
      /*  env.put(Context.SECURITY_PROTOCOL, "ssl");
        String keystore = "C:\\Program Files\\Java\\jdk1.8.0_191\\jre\\lib\\security\\cacerts"; 
    	System.setProperty("javax.net.ssl.trustStore", keystore);
    	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");*/
        try {
            dc = new InitialLdapContext(env, null);
            System.out.println("AD域服务连接认证成功");
        } catch (Exception e) {
            System.out.println("AD域服务连接认证失败");
            e.printStackTrace();
        }
    }
    
    /**
     * @Description:关闭AD域服务连接
     * @author moonxy
     * @date 2018-05-15
     */
    public void close() {
        if (dc != null) {
            try {
                dc.close();
            } catch (NamingException e) {
                System.out.println("NamingException in close():" + e);
            }
        }
    }
    
    /**
     * @Description:新增AD域用户
     * @author moonxy
     * @date 2018-05-15
     */
    public void add(String newUserName) {
        try {
            Attributes attrs = new BasicAttributes(true);
            attrs.put("objectClass", "user");
            attrs.put("samAccountName", newUserName);
            attrs.put("displayName", newUserName);
            attrs.put("userPrincipalName", newUserName + "@hdycjy.com");
            
            dc.createSubcontext("CN=" + newUserName + "," + root, attrs);
            System.out.println("新增AD域用户成功:" + newUserName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("新增AD域用户失败:" + newUserName);
        }
    }

    /**
     * @Description:删除AD域用户
     * @author moonxy
     * @date 2018-05-15
     */
    public void delete(String dn) {
        try {
            dc.destroySubcontext(dn);
            System.out.println("删除AD域用户成功:" + dn);
        } catch (Exception e) {
            System.out.println("删除AD域用户失败:" + dn);
            e.printStackTrace();
        }
    }

    /**
     * @Description:重命名AD域用户
     * @author moonxy
     * @date 2018-05-15
     */
    public boolean renameEntry(String oldDN, String newDN) {
        try {
            dc.rename(oldDN, newDN);
            System.out.println("重命名AD域用户成功");
            return true;
        } catch (NamingException ne) {
            System.out.println("重命名AD域用户失败");
            ne.printStackTrace();
            return false;
        }
    }

    /**
     * @Description:修改AD域用户属性
     * @author moonxy
     * @date 2018-05-15
     */
    public boolean modifyInformation(String dn, String fieldValue) {
        try {
            ModificationItem[] mods = new ModificationItem[1];  
            // 修改属性
            Attribute attr0 = new BasicAttribute("homePhone",fieldValue);  
            //mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr0);//新增属性
            //mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,attr0);//删除属性
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr0);//覆盖属性
            dc.modifyAttributes(dn + "," + root, mods); 
            System.out.println("修改AD域用户属性成功");
            return true;
        } catch (Exception e) {
            System.err.println("修改AD域用户属性失败");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Description:搜索指定节点下的所有AD域用户
     * @author moonxy
     * @date 2018-05-15
     */
    public void searchInformation(String searchBase) {
        try {
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "objectClass=user";
            String returnedAtts[] = { "memberOf" };
            searchCtls.setReturningAttributes(returnedAtts);
            NamingEnumeration<SearchResult> answer = dc.search(searchBase, searchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                System.out.println("<<<::[" + sr.getName() + "]::>>>>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description:指定搜索节点搜索指定域用户
     * @author moonxy
     * @date 2018-05-15
     */
    public SearchResult searchByUserName(String searchBase, String userName) {
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "sAMAccountName=" + userName;
        String returnedAtts[] = { "memberOf" ,"givenName" }; //定制返回属性
        searchCtls.setReturningAttributes(returnedAtts); //设置返回属性集
        try {
            NamingEnumeration<SearchResult> answer = dc.search(searchBase, searchFilter, searchCtls);
            return answer.next();
        } catch (Exception e) {
            System.err.println("指定搜索节点搜索指定域用户失败");
            e.printStackTrace();
        }
        return null;
    }
}
