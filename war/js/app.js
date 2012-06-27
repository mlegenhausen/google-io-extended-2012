(function() {
	'use strict';
	
	_.extend(Backbone.View.prototype, {
		alertTemplate: Handlebars.compile($('#alert-template').html()),
		alertTypes: {
			error: 'Fehler',
			success: 'Erfolg'
		},
		destroy: function() {},
		alert: function(type, msg) {
			this.$('.alert-container').html(this.alertTemplate({
				type: type,
				label: this.alertTypes[type],
				text: msg
			}));
		}
	});
	
	var NotesModel = Backbone.Collection.extend({
		url: '/notes'
	});
	
	var NoteModel = Backbone.Model.extend({
		urlRoot: '/note'
	});
	
	var NoteEditView = Backbone.View.extend({
		events: {
			'click #save': 'save',
			'click #canel': 'cancel'
		},
		initialize: function() {
			_.bindAll(this, 'saveSuccess', 'saveError');
			var source = $('#note-edit-template').html();
			this.template = Handlebars.compile(source);
		},
		render: function() {
			var model = this.model.toJSON();
			this.$el.html(this.template(model));
		},
		save: function() {
			this.model.save({
				title: this.$('input[name=title]').val(),
				content: this.$('textarea[name=content]').val()
			}, {
				success: this.saveSuccess,
				error: this.saveError
			});
			return false;
		},
		saveSuccess: function() {
			this.trigger('save', this.model, this);
		},
		saveError: function(model, resp) {
			if (resp.status === 401) {
				window.location.href = model.get('url');
			} else {
				this.alert('error', 'Notiz konnte nicht hinzugefŸgt werden.');
			}
		},
		cancel: function() {
			this.trigger('cancel', this.model, this);
		}
	});
	
	var NoteEntryView = Backbone.View.extend({
		tagName: 'tr',
		events: {
			'click': 'select',
			'click a': 'remove'
		},
		initialize: function() {
			var source = $("#note-template").html();
			this.template = Handlebars.compile(source);
		},
		render: function() {
			var model = this.model.toJSON();
			this.$el.html(this.template(model));
			return this;
		},
		select: function() {
			this.trigger('select', this.model, this);
		},
		remove: function() {
			this.model.remove();
			this.$el.remove();
		}
	});
	
	var NoteListView = Backbone.View.extend({
		initialize: function() {
			this.model = new NotesModel();
			var source = $("#notes-template").html();
			this.template = Handlebars.compile(source);
			this.model.on('change', this.renderEntries, this);
			this.model.fetch();
		},
		render: function() {
			var model = this.model.toJSON();
			this.$el.html(this.template(model));
			this.renderEntries();
		},
		renderEntries: function() {
			this.$('table > tbody').empty();
			this.model.each(this.renderEntry, this);
		},
		renderEntry: function(model) {
			var view = new NoteEntryView({
				model: model
			});
			this.$('table > tbody').append(view.render().el);
		}
	});
	
	var Router = Backbone.Router.extend({
		routes: {
			"": "showNotes",
			"add": "addNote",
			"edit/:id": "editNote"
		},
		initialize: function() {
			_.bindAll(this, 'show');
			Backbone.history.start();
		},
		showNotes: function() {
			if (this.view) this.view.destroy();
			this.view = new NoteListView({
				el: $('#content')
			});
			this.view.on('select', function(model) {
				this.show('edit/' + model.id);
			}, this);
			this.view.render();
		},
		editNote: function(id) {
			if (this.view) this.view.destroy();
			this.view = new NoteEditView({
				el: $('#content'),
				model: new NoteModel({
					id: id
				})
			});
			this.view.render();
		},
		addNote: function() {
			if (this.view) this.view.destroy();
			this.view = new NoteEditView({
				el: $('#content'),
				model: new NoteModel()
			});
			this.view.on('save', this.show(''), this);
			this.view.render();
		},
		show: function(route) {
			return _.bind(function() {
				if (this.view) this.view.destroy();
				this.navigate(route, { trigger: true });
			}, this);
		}
	});
	
	new Router();
})();