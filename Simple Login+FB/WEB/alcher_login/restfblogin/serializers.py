from rest_framework import serializers
from rest_framework.serializers import (
    ModelSerializer,
    ValidationError,
    EmailField,
)

from restfblogin.models import User


class RegisterSerializer(ModelSerializer):
    email = EmailField(label='Email address')

    class Meta:
        model = User
        fields = [
            'email',
            'fbid',
        ]

    def validate(self, data):
        return data

    def create(self, validated_data):
        email = validated_data['email']
        fbid = validated_data['fbid']
        user_qs = User.objects.filter(email=email)
        user_obj = User(
            email=email,
            fbid=fbid,
        )
        #Save only if new user
        if not user_qs.exists():
            user_obj.save()
        return user_obj
