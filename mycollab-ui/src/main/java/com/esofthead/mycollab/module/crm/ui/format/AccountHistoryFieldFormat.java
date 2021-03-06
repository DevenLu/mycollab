/**
 * This file is part of mycollab-ui.
 *
 * mycollab-ui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-ui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-ui.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.crm.ui.format;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.html.DivLessFormatter;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.utils.HistoryFieldFormat;
import com.esofthead.mycollab.utils.TooltipHelper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Text;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.esofthead.mycollab.utils.TooltipHelper.TOOLTIP_ID;

/**
 * @author MyCollab Ltd
 * @since 5.2.11
 */
public class AccountHistoryFieldFormat implements HistoryFieldFormat {
    private static final Logger LOG = LoggerFactory.getLogger(AccountHistoryFieldFormat.class);

    @Override
    public String toString(String value) {
        return toString(value, true, AppContext.getMessage(GenericI18Enum.FORM_EMPTY));
    }

    @Override
    public String toString(String value, Boolean displayAsHtml, String msgIfBlank) {
        if (StringUtils.isBlank(value)) {
            return msgIfBlank;
        }

        try {
            Integer accountId = Integer.parseInt(value);
            AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
            SimpleAccount account = accountService.findById(accountId, AppContext.getAccountId());

            if (account != null) {
                if (displayAsHtml) {
                    A link = new A().setId("tag" + TOOLTIP_ID);
                    link.setHref(CrmLinkGenerator.generateAccountPreviewFullLink(AppContext.getSiteUrl(), accountId))
                            .appendChild(new Text(account.getAccountname()));
                    link.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(CrmTypeConstants.ACCOUNT,
                            accountId + ""));
                    link.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
                    Div div = new DivLessFormatter().appendChild(new Text(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT).getHtml()),
                            DivLessFormatter.EMPTY_SPACE(), link);
                    return div.write();
                } else {
                    return account.getAccountname();
                }
            }
        } catch (Exception e) {
            LOG.error("Error", e);
        }

        return value;
    }
}
