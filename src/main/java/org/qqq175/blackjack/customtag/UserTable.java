package org.qqq175.blackjack.customtag;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class UserTable extends TagSupport {
	private String head;
	private int rows;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doAfterBody()
	 */
	@Override
	public int doAfterBody() throws JspException {
		if (rows-- > 1) {
			try {
				pageContext.getOut().write("</td></tr><tr><td>");
			} catch (IOException e) {
				throw new JspTagException(e.getMessage());
			}
			return EVAL_BODY_AGAIN;
		} else {
			return SKIP_BODY;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		try {
			pageContext.getOut().write("</td></tr></tbody></table>");
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
			out.write("<table border='1'><colgroup span='2' title='title' />");
			out.write("<caption>" + Locale.getDefault().getDisplayCountry() + "</caption>");
			out.write("<thead><tr><th scope='col'>" + head + "</th></tr></thead>");
			out.write("<tbody><tr><td>");
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * @param head
	 *            the head to set
	 */
	public void setHead(String head) {
		this.head = head;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}

}
