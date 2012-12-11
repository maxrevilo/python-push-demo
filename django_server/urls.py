from django.conf.urls import patterns, url  # , include

urlpatterns = patterns('django_server.views',
    #Page urls:
    url(r'^$', 'index', name='index'),
    url(r'^delete$', 'delete', name='delete'),
    url(r'^send$', 'send', name='send'),

    #Push urls:
    url(r'^register$', 'register', name='register'),
    url(r'^sync$', 'sync', name='sync'),
)
