from django.db import models


class User(models.Model):
    timestamp = models.DateTimeField(auto_now=False, auto_now_add=True)
    fbid = models.CharField(max_length=400, default=0)
    email = models.CharField(max_length=400, default=0)

    def __str__(self):
        return self.email
