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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.common.domain.OptionVal;
import com.esofthead.mycollab.common.service.OptionValService;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.ui.chart.PieChartWrapper;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@ViewComponent
public class TaskStatusChartWidget extends PieChartWrapper<TaskSearchCriteria> implements ITaskStatusChartWidget {
    public TaskStatusChartWidget() {
        super(OptionI18nEnum.TaskPriority.class, 350, 280);
    }

    @Override
    protected DefaultPieDataset createDataset() {
        // create the dataset...
        final DefaultPieDataset dataset = new DefaultPieDataset();

        OptionValService optionValService = AppContextUtil.getSpringBean(OptionValService.class);
        List<OptionVal> optionVals = optionValService.findOptionVals(ProjectTypeConstants.TASK,
                CurrentProjectVariables.getProjectId(), AppContext.getAccountId());
        for (OptionVal optionVal : optionVals) {
            if (com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum.Closed.name().equals(optionVal.getTypeval())) {
                continue;
            }
            boolean isFound = false;
            for (GroupItem item : groupItems) {
                if (optionVal.getTypeval().equals(item.getGroupid())) {
                    dataset.setValue(optionVal.getTypeval(), item.getValue());
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                dataset.setValue(optionVal.getTypeval(), 0);
            }
        }

        return dataset;
    }

    @Override
    protected List<GroupItem> loadGroupItems() {
        ProjectTaskService taskService = AppContextUtil.getSpringBean(ProjectTaskService.class);
        return taskService.getStatusSummary(searchCriteria);
    }

    @Override
    public void clickLegendItem(String key) {
        TaskSearchCriteria cloneSearchCriteria = BeanUtility.deepClone(searchCriteria);
        cloneSearchCriteria.setStatuses(new SetSearchField<>(key));
        EventBusFactory.getInstance().post(new TaskEvent.SearchRequest(this, cloneSearchCriteria));
    }
}
