package com.controller;

import java.util.Properties;
import javax.naming.*;
import javax.naming.ldap.*;
import javax.naming.directory.*;

public class Client {
	public static void main(String[] args) {

		Properties env = new Properties();
		String adminName = "CN=Administrator,CN=Users,DC=hdycjy,DC=com";
		String adminPassword = "huagongYCJY@789.com";
		String ldapURL = "LDAP://172.25.1.17:636";
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		// set security credentials, note using simple cleartext authentication
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, adminName);
		env.put(Context.SECURITY_CREDENTIALS, adminPassword);
		// connect to my domain controller
		env.put(Context.PROVIDER_URL, ldapURL);
		 env.put(Context.SECURITY_PROTOCOL, "ssl");

		try {
			// Create the initial directory context
			LdapContext ctx = new InitialLdapContext(env, null);

			// Create the search controls
			SearchControls searchCtls = new SearchControls();

			// Specify the search scope
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// specify the LDAP search filter
			String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*))";

			// Specify the Base for the search
			String searchBase = "OU=学生,OU=学院,OU=NEC,OU=Person,OU=vmware,DC=hdycjy,DC=com";
			// initialize counter to total the group members
			int totalResults = 0;
			// Specify the attributes to return
			String returnedAtts[] = { "memberOf" };
			searchCtls.setReturningAttributes(returnedAtts);

			// Search for objects using the filter
			NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
			// Loop through the search results
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				System.out.println(">>>" + sr.getName());
				// Print out the groups
				Attributes attrs = sr.getAttributes();
				if (attrs != null) {
					try {
						for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
							Attribute attr = (Attribute) ae.next();
							// System.out.println("Attribute: " + attr.getID());
							for (NamingEnumeration e = attr.getAll(); e.hasMore(); totalResults++) {
								System.out.println(e.next());
							}
						}
					} catch (NamingException e) {
						e.printStackTrace();
						System.err.println("Problem listing membership: " + e);
					}

				}
			}
			System.out.println("Total groups: " + totalResults);
			ctx.close();
		}

		catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Problem searching directory: " + e);
		}
	}

}
