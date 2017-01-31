package org.qqq175.blackjack.customtag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.qqq175.blackjack.StringConstant;
import org.qqq175.blackjack.persistence.dao.util.PhotoManager;
import org.qqq175.blackjack.persistence.entity.User;

/**
 * Custom tag to define user avatar path and output it as <img> tag
 * @author qqq175
 */
public class UserPhotoTag extends TagSupport {
	/**
	 * 
	 */
	private static PhotoManager photoManager = new PhotoManager();
	private static final long serialVersionUID = 1L;
	private User user;
	private String className;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			/* reset params for enable proper reuse*/
			user = null;
			className = null;
			JspWriter out = pageContext.getOut();
			out.write(">");
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		return EVAL_PAGE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			out.write("<img src='");
			/* if user param isn't defined - take user from session */
			if (user == null) {
				user = (User) pageContext.getSession().getAttribute(StringConstant.ATTRIBUTE_USER);
				System.out.println("TAG user - session");
			}
			/* get user avatar path */
			if (user != null) {
				out.write(photoManager.findPhotoRelativePath(user.getId()));
				out.write("' alt='");
				out.write(user.getDisplayName());
			} else {
				out.write("' alt='");
				out.write("no image");
				System.out.println("TAG user - null");
			}
			out.write("'");
			/* if className is defined - output tag css class*/
			if (className != null) {
				out.write(" class='");
				out.write(className);
				out.write("'");
			}
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

}
