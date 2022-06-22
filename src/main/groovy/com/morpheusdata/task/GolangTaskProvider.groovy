package com.morpheusdata.task

import com.morpheusdata.core.*
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.TaskType
import com.morpheusdata.model.Task

/**
 * Example TaskProvider
 */
class GolangTaskProvider implements TaskProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	AbstractTaskService service
	Task task

	GolangTaskProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
		this.task = task
	}

	

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	@Override
	ExecutableTaskInterface getService() {
		return new GolangTaskService(morpheusContext)
	}

	@Override
	String getCode() {
		return "golang-task"
	}

	@Override
	TaskType.TaskScope getScope() {
		return TaskType.TaskScope.all
	}

	@Override
	String getName() {
		return 'Golang Script'
	}

	@Override
	String getDescription() {
		return 'Golang script execution task'
	}

	@Override
	Boolean isAllowExecuteLocal() {
		return true
	}

	@Override
	Boolean isAllowExecuteRemote() {
		return false
	}

	@Override
	Boolean isAllowExecuteResource() {
		return false
	}

	@Override
	Boolean isAllowLocalRepo() {
		return false
	}

	@Override
	Boolean isAllowRemoteKeyAuth() {
		return false
	}

	@Override
	Boolean hasResults() {
		return true
	}

	@Override
	List<OptionType> getOptionTypes() {
		OptionType optionType = new OptionType(
				name: 'golangScript',
				code: 'golangTaskScript',
				fieldName: 'golangScriptField',
				displayOrder: 0,
				fieldLabel: 'Golang Script',
				required: true,
				inputType: OptionType.InputType.CODE_EDITOR
		)
		return [optionType]
	}

}
