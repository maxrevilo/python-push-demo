from django.http import HttpResponse
from django.shortcuts import render_to_response, get_object_or_404, redirect
from django.db import IntegrityError
from django_server.models import *

from python_push.message import Message
from python_push.gcm.gcm_push_service import GCMPushService
from python_push.blackberry.bb_push_service import BBPushService
from python_push.push_manager import PushManager


### Page views: ###
def index(request):
    return render_to_response('index.html', {'devices': Device.objects.all()})


def delete(request):
    dev = get_object_or_404(Device, id=request.GET['id'])
    dev.delete()
    return redirect('index')


def send(request):
    #Extracts the devices ids and converts it to integers:
    device_ids = map(lambda id: int(id), request.POST.getlist('device'))

    if len(device_ids) > 0:
        device_list = Device.objects.filter(id__in=device_ids)

        push_manager = PushManager([
            GCMPushService({'api_id': 'AIzaSyCwR74jMF8Ls0CXvJzKHMpHVQzwml9xmTI'}),
            BBPushService({'api_id': '2974-Mie72996c2B7m3t87M17o8172i80r505273', 'password': 'dsvoolM5'})
        ])

        # if exists data to be sync we save it
        sync_content = request.POST['scontent']
        if sync_content != '':
            for dev in device_list:
                chunk = Chunck(device=dev, data=sync_content)
                chunk.save()

        status_dict = push_manager.send(Message(), device_list)

        errors = ''
        for type, status in status_dict.iteritems():
            print 'Service %s: Status %i, Content %s\n' % (type, status.code, status.raw)
            if not status.is_ok:
                if errors == '':
                    errors = 'Error sending request.<br>'
                errors += '&lt;Type: %s Code: %i&#92;\&gt;<br>Content:<br>%s<br><br>' % (type, status.code, status.raw)

        if errors == '':
            return redirect('index')
        else:
            return HttpResponse(errors)

    else:
        return HttpResponse('Select a device to send the push')


### Push views: ###
def register(request):
    dev = None
    try:
        dev = Device(token=request.POST['token'], type=request.POST['type'])
        dev.save()

    except IntegrityError:
        dev = Device.objects.get(token=request.POST['token'], type=request.POST['type'])

    return HttpResponse('id=%i' % dev.id)


def sync(request):
    """A device ask for data waiting to be sync"""
    #dev = get_object_or_404(Device, id=request.GET['id'])
    dev = get_object_or_404(Device, token=request.POST['token'], type=request.POST['type'])
    chuncks = Chunck.objects.filter(device=dev)
    result = ''

    if len(chuncks) > 0:
        for chunck in chuncks:
            result += chunck.data + '\n'

        chuncks.delete()
        return HttpResponse(result)
    else:
        return HttpResponse('Nothing to Sync')
