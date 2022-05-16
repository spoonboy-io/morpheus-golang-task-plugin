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
		return new GolangTaskService(morpheus)
	}

	@Override
	String getCode() {
		return "golangTask"
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
		return true
	}

	@Override
	Boolean isAllowRemoteKeyAuth() {
		return true
	}

	@Override
	Boolean hasResults() {
		return true
	}

	/**
	 * Builds an OptionType to take some text
	 * @return list of OptionType
	 */
	@Override
	List<OptionType> getOptionTypes() {
		OptionType optionType = new OptionType(
				name: 'golangScript',
				code: 'golangTaskScript',
				fieldName: 'golangScriptField',
				//optionSource: true,
				displayOrder: 0,
				fieldLabel: 'Golang Script',
				required: true,
				inputType: OptionType.InputType.CODE_EDITOR
		)
		return [optionType]
	}

}
