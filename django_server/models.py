from django.db import models
from python_push.gcm.gcm_push_service import GCMPushService


class Device(models.Model):
    """A mobile Device"""
    TYPES = (
        (GCMPushService.type, 'Google Android'),
        ('BO', 'BlackBerry OS'),
        ('AI', 'Apple iOs'),
        ('WP', 'Windows Phone 7')
    )

    token = models.CharField(max_length=256)
    type = models.CharField(max_length=2, choices=TYPES)

    class Meta:
        unique_together = ('token', 'type')


class Chunck(models.Model):
    """A chunk of data waiting to be sync"""
    device = models.ForeignKey('Device')
    data = models.CharField(max_length=1024)
