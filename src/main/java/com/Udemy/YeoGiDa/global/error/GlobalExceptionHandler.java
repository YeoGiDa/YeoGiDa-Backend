package com.Udemy.YeoGiDa.global.error;

import com.Udemy.YeoGiDa.domain.follow.exception.AlreadyFollowException;
import com.Udemy.YeoGiDa.domain.heart.exception.AlreadyHeartException;
import com.Udemy.YeoGiDa.domain.heart.exception.HeartNotFoundException;
import com.Udemy.YeoGiDa.domain.member.exception.AlreadyExistsNicknameException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberDuplicateException;
import com.Udemy.YeoGiDa.domain.member.exception.MemberNotFoundException;
import com.Udemy.YeoGiDa.domain.place.exception.PlaceNotFoundException;
import com.Udemy.YeoGiDa.domain.trip.exception.TripImgEssentialException;
import com.Udemy.YeoGiDa.domain.trip.exception.TripNotFoundException;
import com.Udemy.YeoGiDa.global.error.dto.ErrorResult;
import com.Udemy.YeoGiDa.global.exception.ForbiddenException;
import com.Udemy.YeoGiDa.global.exception.NotFoundException;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenHasExpiredException;
import com.Udemy.YeoGiDa.global.jwt.exception.TokenIsInvalidException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ErrorResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ErrorResult(400, "Validation Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ErrorResult MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        return new ErrorResult(400, "TypeMismatch Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ErrorResult handleIllegalArgumentException(IllegalArgumentException e) {
        return new ErrorResult(400, "IllegalArgument Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ErrorResult handleMemberDuplicateException(MemberDuplicateException e){
        return new ErrorResult(400, "MemberDuplicate Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ErrorResult handleAlreadyExistsNicknameException(AlreadyExistsNicknameException e){
        return new ErrorResult(400, "AlreadyExistsNickname Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ErrorResult handleAlreadyLikedException(AlreadyHeartException e) {
        return new ErrorResult(400, "AlreadyLiked Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ErrorResult handleAlreadyLikedException(AlreadyFollowException e) {
        return new ErrorResult(400, "AlreadyFollowed Error!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    protected ErrorResult handleAlreadyLikedException(TripImgEssentialException e) {
        return new ErrorResult(400, "TripImgEssential Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected ErrorResult handleForbiddenException(ForbiddenException e){
        return new ErrorResult(403, "Forbidden Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected ErrorResult handleJwtException(JwtException e){
        return new ErrorResult(403, "JwtInvalid Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected ErrorResult handleTokenHasExpiredException(TokenHasExpiredException e){
        return new ErrorResult(403, "TokenHasExpired Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected ErrorResult handleTokenIsInvalidException(TokenIsInvalidException e){
        return new ErrorResult(403, "TokenIsInvalid Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected ErrorResult handleNotFoundException(NotFoundException e){
        return new ErrorResult(404, "NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected ErrorResult handleNotFoundException(MemberNotFoundException e){
        return new ErrorResult(404, "Member NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected ErrorResult handleNotFoundException(TripNotFoundException e){
        return new ErrorResult(404, "Trip NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected ErrorResult handleNotFoundException(PlaceNotFoundException e){
        return new ErrorResult(404, "Place NotFound Error!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    protected ErrorResult handleNotFoundException(HeartNotFoundException e){
        return new ErrorResult(404, "Heart NotFound Error!");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler
    protected ErrorResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return new ErrorResult(405, "HttpMethod Error!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    protected ErrorResult handleFileUploadException(FileUploadException e){
        return new ErrorResult(500, "FileUpload Error!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    protected ErrorResult handleAmazonS3Exception(AmazonS3Exception e){
        return new ErrorResult(500, "AmazonS3 Error!");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    protected ErrorResult handleException(Exception e){
        return new ErrorResult(500, "Global Exception Error!", e.getMessage());
    }
}
