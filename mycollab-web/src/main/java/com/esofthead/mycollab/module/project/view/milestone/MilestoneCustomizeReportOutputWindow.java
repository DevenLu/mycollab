/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.core.db.query.VariableInjector;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleMilestone;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.service.MilestoneService;
import com.esofthead.mycollab.reporting.CustomizeReportOutputWindow;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;

import java.util.Collection;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class MilestoneCustomizeReportOutputWindow extends CustomizeReportOutputWindow<MilestoneSearchCriteria, SimpleMilestone> {
    public MilestoneCustomizeReportOutputWindow(VariableInjector<MilestoneSearchCriteria> variableInjector) {
        super(ProjectTypeConstants.MILESTONE, AppContext.getMessage(MilestoneI18nEnum.LIST), SimpleMilestone.class,
                AppContextUtil.getSpringBean(MilestoneService.class), variableInjector);
    }

    @Override
    protected Collection<TableViewField> getDefaultColumns() {
        return null;
    }

    @Override
    protected Collection<TableViewField> getAvailableColumns() {
        return null;
    }

    @Override
    protected Object[] buildSampleData() {
        return new Object[0];
    }
}
